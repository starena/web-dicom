package dicomcl;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

public class DirTreePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	public static final ImageIcon ICON_COMPUTER = new ImageIcon("images/computer.gif");
	public static final ImageIcon ICON_DISK = new ImageIcon("images/disk.gif");
	public static final ImageIcon ICON_FOLDER = new ImageIcon("images/folder.gif");
	
	public JTree  dirTree;
	private DefaultTreeModel treeModel;
	private JScrollPane scrollpane;
	private File selectedDir;
	
	public DirTreePanel()
	{
		setBorder (BorderFactory.createTitledBorder (new LineBorder(Color.BLACK),"Computer"));
		setLayout (new BorderLayout());
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode (new IconData(ICON_COMPUTER, null, "Computer"));
		DefaultMutableTreeNode node;
		File [] roots = File.listRoots();
		for (int k=0; k<roots.length; k++)
		{
			node = new DefaultMutableTreeNode (new IconData(ICON_DISK,null, new FileNode(roots[k])));
			top.add(node);
			node.add (new DefaultMutableTreeNode(new Boolean(true)));
		}
		treeModel = new DefaultTreeModel(top);
		dirTree = new JTree (treeModel);
		dirTree.putClientProperty ("JTree.lineStyle","Angled");
		TreeCellRenderer renderer = new IconCellRenderer();
		dirTree.setCellRenderer(renderer);
		dirTree.addTreeExpansionListener (new DirExpansionListener());
		dirTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION); 
		dirTree.setShowsRootHandles(true); 
		dirTree.setEditable(false);
		
		scrollpane = new JScrollPane (dirTree);
		add (scrollpane);
		
		setPreferredSize(new Dimension (120,120));
		setMinimumSize(new Dimension (120,120));
		setVisible(true);
	}

	public File getSelectedDir()
	{
		return selectedDir;
	}
	
	public DefaultMutableTreeNode getTreeNode (TreePath path)
	{
		return (DefaultMutableTreeNode)(path.getLastPathComponent());
	}

	public FileNode getFileNode(DefaultMutableTreeNode node) 
	{
		if (node == null) return null;
		Object obj = node.getUserObject();
		if (obj instanceof IconData) obj = ((IconData) obj).getObject();
		if (obj instanceof FileNode) return (FileNode) obj;
		else return null;
	}

	private class DirExpansionListener implements TreeExpansionListener
	{
		public void treeExpanded(TreeExpansionEvent event)
		{
			final DefaultMutableTreeNode node = getTreeNode(event.getPath());
			final FileNode fnode = getFileNode(node);
	
			Thread runner = new Thread() 
			{
				public void run() 
				{
					if (fnode != null && fnode.expand(node)) 
					{
						Runnable runnable = new Runnable() 
						{
							public void run() 
							{
								treeModel.reload(node);
							}
						};
						SwingUtilities.invokeLater(runnable);
					}
				}
			};
			runner.start();
		}
		public void treeCollapsed(TreeExpansionEvent event) {}
	}
}

class IconCellRenderer extends JLabel implements TreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	private Color m_textSelectionColor;
	private Color m_textNonSelectionColor;
	private Color m_bkSelectionColor;
	private Color m_bkNonSelectionColor;
	private Color m_borderSelectionColor;
	private boolean m_selected;

	public IconCellRenderer()
	{
		super();
		m_textSelectionColor = UIManager.getColor ("Tree.selectionForeground");
		m_textNonSelectionColor = UIManager.getColor ("Tree.textForeground");
		m_bkSelectionColor = UIManager.getColor ("Tree.selectionBackground");
		m_bkNonSelectionColor = UIManager.getColor ("Tree.textBackground");
		m_borderSelectionColor = UIManager.getColor ("Tree.selectionBorderColor");
		setOpaque(false);
	}

	public Component getTreeCellRendererComponent (JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Object obj = node.getUserObject();
		setText(obj.toString());
		if (obj instanceof Boolean) setText("Retrieving data...");
		if (obj instanceof IconData)
		{
			IconData idata = (IconData)obj;
			if (expanded) setIcon(idata.getExpandedIcon());
			else setIcon(idata.getIcon());
		}
		else setIcon(null);
		setFont(tree.getFont());
		setForeground(sel?m_textSelectionColor:m_textNonSelectionColor);
		setBackground(sel?m_bkSelectionColor:m_bkNonSelectionColor);
		m_selected = sel;
		return this;
	}
    
	public void paintComponent (Graphics g) 
	{
		Color bColor = getBackground();
		Icon icon = getIcon();
		g.setColor(bColor);
		int offset = 0;
		if (icon!=null && getText()!=null) offset = (icon.getIconWidth()+getIconTextGap());
		g.fillRect (offset,0,getWidth()-1-offset,getHeight()-1);
		if (m_selected) 
		{
			g.setColor (m_borderSelectionColor);
			g.drawRect (offset,0,getWidth()-1-offset,getHeight()-1);
		}
		super.paintComponent(g);
	}
}

class IconData
{
	private  Icon m_icon;
	private  Icon m_expandedIcon;
	private  Object m_data;
	
	public IconData (Icon icon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = null;
		m_data = data;
	}
	public IconData (Icon icon, Icon expandedIcon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = expandedIcon;
		m_data = data;
	}
	public Icon getIcon() 
	{ 
		return m_icon;
	}

	public Icon getExpandedIcon() 
	{ 
		return m_expandedIcon!=null ? m_expandedIcon : m_icon;
	}
	
	public Object getObject() 
	{ 
		return m_data;
	}
	
	public String toString() 
	{ 
		return m_data.toString();
	}
}

class FileNode
{
	private  File m_file;
	
	public FileNode(File file)
	{
		m_file = file;
	}
	public File getFile() 
	{ 
		return m_file;
	}
	public String toString() 
	{ 
		return m_file.getName().length()>0?m_file.getName():m_file.getPath();
	}


	@SuppressWarnings("unchecked")
	public boolean expand (DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode flag = (DefaultMutableTreeNode)parent.getFirstChild();
		if (flag==null) return false;
		Object obj = flag.getUserObject();
		if (!(obj instanceof Boolean)) return false;
		parent.removeAllChildren();
		File[] files = listFiles();
		if (files == null) return true;
		Vector v = new Vector();
		for (int k=0; k<files.length; k++)
		{
			File f = files[k];
			if (!(f.isDirectory())) continue;
			FileNode newNode = new FileNode(f);
			boolean isAdded = false;
			for (int i=0; i<v.size(); i++)
			{
				FileNode nd = (FileNode)v.elementAt(i);
				if (newNode.compareTo(nd)<0)
				{
					v.insertElementAt (newNode,i);
					isAdded = true;
					break;
				}
			}
			if (!isAdded) v.addElement(newNode);
		}
		for (int i=0; i<v.size(); i++)
		{
			FileNode nd = (FileNode)v.elementAt(i);
			IconData idata = new IconData (DirTreePanel.ICON_FOLDER,DirTreePanel.ICON_FOLDER,nd);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
			parent.add(node);
			if (nd.hasSubDirs()) node.add(new DefaultMutableTreeNode (new Boolean(true) ));
		}
		return true;
	}
	
	public boolean hasSubDirs()
	{
		File[] files = listFiles();
		if (files == null) return false;
		for (int k=0; k<files.length; k++)
		{
			if (files[k].isDirectory()) return true;
		}
		return false;
	}
	
	public int compareTo(FileNode toCompare)
	{ 
		return  m_file.getName().compareToIgnoreCase (toCompare.m_file.getName() ); 
	}
	
	protected File[] listFiles()
	{
		if (!m_file.isDirectory()) return null;
		try
		{
			return m_file.listFiles();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog (null,"Error reading directory "+m_file.getAbsolutePath(),"Warning",JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
}
