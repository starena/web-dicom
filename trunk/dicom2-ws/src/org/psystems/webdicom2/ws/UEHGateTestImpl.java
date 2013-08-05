package org.psystems.webdicom2.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroup;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupFormulaOperand;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupPI;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupRequestFilter;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcLimit;

/**
 * Тестовая заглушка
 * 
 * @author dima_d
 * 
 */
public class UEHGateTestImpl extends UEHGate {

	public static Logger logger = Logger.getLogger(UEHGateTestImpl.class
			.getName());

	Calendar cal = Calendar.getInstance();
	{
		cal.set(2012, 1, 3, 4, 5, 6);
	}

//	@Override
//	public void setLimit(CalcLimit limit) throws WsException {
//
//		if (limit.idCalcgroup == 2 && limit.idCalcgroupLimit == 3
//				&& limit.date.compareTo(cal.getTime()) == 0
//				&& "P".equals(limit.typeProf) && limit.maxR == 100
//				&& limit.maxY == 200) {
//			return;
//		}
//
//		String msg = "Limit must be: [idCalcgroup=2, idCalcgroupLimit=3, date=2012/02/02 04:05:06, type=P, maxR=100, maxY=200]";
//
//		Util.throwWsException(logger, "Error test data! " + msg,
//				new IllegalArgumentException(msg));
//	}

	@Override
	public CalcGroup newCalcGroup(CalcGroupPI calcgroup) throws WsException {
		// TODO Auto-generated method stub
		if (calcgroup.idSAPPIGroup == 2 && calcgroup.idSAPPIGroupLimit == 3
				&& calcgroup.date.compareTo(cal.getTime()) == 0
				&& "P".equals(calcgroup.type) && "171000000".equals(calcgroup.ownerId)
				&& "171002342".equals(calcgroup.typeLimitId)) {
			String msg = "CalcGroupPI must be: [idSapGroup=2, idSapGroupLimit=3, date=2012/02/02 04:05:06, type=P, owner=oao, typeLimit=ТЭК]";
			Util.throwWsException(logger, "Error test data! " + msg,
					new IllegalArgumentException(msg));
		}

		CalcGroup cg = new CalcGroup();
		cg.id = 2;
//		cg.idCalcGroupLimit = 3;
		cg.date = cal.getTime();

		String msg = "CalcGroup [id=2, idCalcGroupLimit=3, date=2012/02/02 04:05:06]\n";
		msg += "CalcGroupPI: " + calcgroup;

		logger.log(Level.WARNING, " Test newCalcGroup(...) returned: " + msg);

		return cg;
	}

	@Override
	public CalcGroup getCalcGroup(int idSapGroup) throws WsException {

		CalcGroup cg = new CalcGroup();
		cg.id = idSapGroup + 1000;
//		cg.idCalcGroupLimit = idSapGroup + 2000;

		// operands
		ArrayList<CalcGroupFormulaOperand> formula = new ArrayList<CalcGroupFormulaOperand>();
		for (int oper = 1; oper <= 10; oper++) {

			CalcGroupFormulaOperand operand = new CalcGroupFormulaOperand();
			operand.objId = oper;
			operand.parId = oper + 100;
			if (oper % 2 == 0)
				operand.operator = "+";
			else
				operand.operator = "-";

			formula.add(operand);

		}

		return cg;

	}

	public ArrayList<CalcGroup> getCalcGroups(
			ArrayList<CalcGroupRequestFilter> filters) throws WsException {

		StringBuffer msg = new StringBuffer();

		ArrayList<CalcGroup> result = new ArrayList<CalcGroup>();
		for (int group = 1; group <= 10; group++) {
			CalcGroup cg = new CalcGroup();
			cg.id = group;
//			cg.idCalcGroupLimit = group + 1000;

			// operands
			ArrayList<CalcGroupFormulaOperand> formula = new ArrayList<CalcGroupFormulaOperand>();
			for (int oper = 1; oper <= 10; oper++) {

				CalcGroupFormulaOperand operand = new CalcGroupFormulaOperand();
				operand.objId = oper;
				operand.parId = oper + 100;
				if (oper % 2 == 0)
					operand.operator = "+";
				else
					operand.operator = "-";

				formula.add(operand);

			}

			cg.formula = formula;
			result.add(cg);
			msg.append(cg.toString());
			msg.append("\n");
		}

		logger.log(Level.WARNING,
				" Test getAllCalcGroups() returned: \n" + msg.toString());

		return result;
	}

}
