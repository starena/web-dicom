package org.psystems.dicom.commons.orm;

import java.util.ArrayList;

import junit.framework.TestCase;

public class DiagnosisTest extends TestCase {

	public void testToPersistentString() {
		Diagnosis dia = new Diagnosis();
		dia.setDiagnosisCode("M01.1");
		dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
		dia.setDiagnosisSubType("Предварительный");
		dia.setDiagnosisDescription("Заболевание M01.1^диагноз");
		
		assertEquals(dia.toPersistentString(),
			"ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1#####диагноз");
		dia.setDiagnosisDescription("Заболевание M01.1|диагноз");
		
		assertEquals(dia.toPersistentString(),
			"ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1@@@@@диагноз");
		
		dia.setDiagnosisDescription("Заболевание M01.1^|^|диагноз");
		assertEquals(dia.toPersistentString(),
			"ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1#####@@@@@#####@@@@@диагноз");
		
		dia.setDiagnosisCode("M01.1 ^|");
		dia.setDiagnosisType(Diagnosis.TYPE_MAIN + " ^|");
		dia.setDiagnosisSubType("Предварительный ^|");
		dia.setDiagnosisDescription("Заболевание M01.1^диагноз ^|");
		
		assertEquals(dia.toPersistentString(),
		"ОСНОВНОЙ #####@@@@@^Предварительный #####@@@@@^M01.1 #####@@@@@^Заболевание M01.1#####диагноз #####@@@@@");
		
	}

	public void testGetFromPersistentString() {
		assertNull(Diagnosis.getFromPersistentString(null));
		assertNull(Diagnosis.getFromPersistentString(""));
		
		Diagnosis dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1");
		assertEquals(dia.getDiagnosisType(), "ОСНОВНОЙ");
		assertEquals(dia.getDiagnosisSubType(), "Предварительный");
		assertEquals(dia.getDiagnosisCode(), "M01.1");
		assertEquals(dia.getDiagnosisDescription(), "Заболевание M01.1");
		
		dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Описание");
		assertEquals(dia.getDiagnosisType(), "ОСНОВНОЙ");
		assertEquals(dia.getDiagnosisSubType(), "Предварительный");
		assertEquals(dia.getDiagnosisCode(), "M01.1");
		assertEquals(dia.getDiagnosisDescription(), "Описание");
		
		dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Описание ##### диагноза");
		assertEquals(dia.getDiagnosisDescription(), "Описание ^ диагноза");
		
		dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Описание ##### @@@@@ диагноза");
		assertEquals(dia.getDiagnosisDescription(), "Описание ^ | диагноза");
		
		try {
			dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^");
			fail("Wrong pattern");
		} catch (IllegalArgumentException ex) {}
		try {
			dia = Diagnosis.getFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^");
			fail("Wrong pattern");
		} catch (IllegalArgumentException ex) {}
		
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
	
	public void testGetCollectionFromPersistentString() {

		Diagnosis[] dias = Diagnosis
				.getCollectionFromPersistentString("ОСНОВНОЙ^Предварительный^M01.1^Заболевание M01.1|СОПУТСТВУЮЩИЙ^Заключительный^K01.1^Заболевание K01.1");

		assertEquals(dias[0].getDiagnosisCode(), "M01.1");
		assertEquals(dias[0].getDiagnosisType(), "ОСНОВНОЙ");
		assertEquals(dias[0].getDiagnosisSubType(), "Предварительный");
		assertEquals(dias[0].getDiagnosisDescription(), "Заболевание M01.1");

		assertEquals(dias[1].getDiagnosisCode(), "K01.1");
		assertEquals(dias[1].getDiagnosisType(), "СОПУТСТВУЮЩИЙ");
		assertEquals(dias[1].getDiagnosisSubType(), "Заключительный");
		assertEquals(dias[1].getDiagnosisDescription(), "Заболевание K01.1");
	}

}
