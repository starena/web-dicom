package dicomcl;

public class Main 
{
	public static void main(String[] args) 
	{
		java.awt.EventQueue.invokeLater (new Runnable()
		{
            public void run() 
            {
                new MainWindow().setVisible(true);
            }
        });
	}
}
