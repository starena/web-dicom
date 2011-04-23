package org.psystems.dicom.commons.orm;

import java.util.ArrayList;

import junit.framework.TestCase;

public class DiagnosisTest extends TestCase {

	public void testToPersistentString() {
		fail("Not yet implemented");
	}

	public void testGetFromPersistentString() {
		Diagnosis.getFromPersistentString("");
	}

	public void testGetCollectionFromPersistentString() {
		Diagnosis[] dias = Diagnosis
				.getCollectionFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1|СОПУТСТВУЮЩИЙ^Заключительный^K01.1^Заболевание K01.1");
		assertEquals(dias[0].getDiagnosisCode(), "M01.1");
		assertEquals(dias[0].getDiagnosisType(), "ОСНОВНОЙ");
		assertEquals(dias[0].getDiagnosisSubType(), "Предварительный");
		assertEquals(dias[0].getDiagnosisDescription(), "Заболевание M01.1");
	}

	public void testToPersistentCollectionString() {

		ArrayList<Diagnosis> dias = new ArrayList<Diagnosis>();

		Diagnosis dia = new Diagnosis();
		dia.setDiagnosisCode("M01.1");
		dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
		dia.setDiagnosisDescription("Заболевание M01.1");
		dia.setDiagnosisSubType("Предварительный");
		dias.add(dia);

		dia = new Diagnosis();
		dia.setDiagnosisCode("K01.1");
		dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
		dia.setDiagnosisDescription("Заболевание K01.1");
		dia.setDiagnosisSubType("Заключительный");
		dias.add(dia);

		assertEquals(
				Diagnosis.toPersistentCollectionString(dias
						.toArray(new Diagnosis[dias.size()])),
				"ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1|СОПУТСТВУЮЩИЙ^Заключительный^K01.1^Заболевание K01.1");

	}

}
