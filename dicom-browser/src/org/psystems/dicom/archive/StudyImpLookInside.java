/*
 3495 - группа 'пациент'

3495 0012 PATIENT_NAME ФИО пациента
3495 0022 PATIENT_NAME_R ФИО пациента
3495 0032 PATIENT_ID ID пациента
3495 0042 PATIENT_SEX Пол
3495 0052 PATIENT_CASE_HISTORY_NUMBER История болезни
3495 0062 PATIENT_ADDRESS_REGION Адрес пац-та (Область)
3495 0072 PATIENT_ADDRESS_AREA Адрес пац-та (Район)
3495 0082 PATIENT_ADDRESS_CITY Адрес пац-та (Город)
3495 0092 PATIENT_ADDRESS_SHF Адрес п-та (Ул./дом/кв.)
3495 00A2 PATIENT_CATHEGORY Категория пациента
3495 00B2 PATIENT_DOB Дата рождения

3497 - группа 'исследование'

3497 0002 STUDY_DESCRIPTION Название ис-ния
3497 000A STUDY_ID ID исследования
3497 0012 STUDY_DATE Дата исследования
3497 0022 STUDY_TIME Время исследования
3497 0032 STUDY_REFERRING_PHYSICIANS_NAME Направивший врач(eng)
3497 0042 STUDY_REFERRING_PHYSICIANS_NAMR Направивший врач
3497 0052 STUDY_LABORANT Рентгенолаборант
3497 0062 STUDY_MD Врач
3497 0072 STUDY_COMMENT Комментарий
3497 0082 STUDY_RESULT Заключение
3497 0092 STUDY_REFERENCE Кем направлен
3497 00A2 STUDY_DIAGNOSIS Диагноз
3497 00B2 STUDY_AREA Область исследования
3497 00C2 STUDY_PROTOCOL Протокол
3497 00D2 STUDY_DOZE Доза
3497 00F2 STUDY_CONTRAST Вид контраста
3497 0102 STUDY_TYPE Вид исследования
3497 0112 STUDY_SOURCE Источник
3497 0132 STUDY_CABINET_PROFILE Профиль кабинета
3497 0142 STUDY_CABINET_NAME Номер кабинета
3497 0152 STUDY_LPU ЛПУ
3497 0162 STUDY_MOVEMENT Движение
3497 0172 STUDY_DETECTED_PATHOLOGY Выявленная патология
3497 0182 STUDY_COLLIMATOR Коллиматор
3497 0192 STUDY_RADIOFARMPREPARAT Радиофармпрепарат
3497 01A2 STUDY_EFFECTIVE_DOZE Эффективная доза
3497 01B2 STUDY_ENTERED_DOZE Объем контраста
3497 01C2 STUDY_MEDSESTRA Медсестра
3497 01D2 STUDY_CITO Неотложное исследование
3497 01E2 STUDY_HARD Сложное исследование
3497 01F2 STUDY_CONTRAST_INTRAVENOUS Контраст
3497 0202 STUDY_CHARGES Нагрузка персонала
3497 0212 STUDY_GENERAL_ANAESTHESIA Наркоз
3497 0222 STUDY_CHILD Ребёнок до 5 лет
3497 0232 STUDY_COMPARISION Сравнение в динамике
3497 0242 STUDY_DICOM_PRINTED Напечатано на DICOM
3497 0252 STUDY_PAPER_PRINTED Напечатано на бумаге
3497 0262 STUDY_ADDITIONAL_FIELD_1 Дополнительное поле 1
3497 0272 STUDY_ADDITIONAL_FIELD_2 Дополнительное поле 2
3497 0282 STUDY_ADDITIONAL_FIELD_3 Дополнительное поле 3
3497 0292 STUDY_ADDITIONAL_FIELD_4 Дополнительное поле 4
3497 02A2 STUDY_ADDITIONAL_FIELD_5 Дополнительное поле 5
3497 02B2 STUDY_ADDITIONAL_FIELD_6 Дополнительное поле 6
3499 - группа 'серия'
3499 0002 SERIES_MODALITY Модальность серии
3499 0010 SeriesDescription Описание серии (всегда 'Study Info')

 */
package org.psystems.dicom.archive;

import java.sql.Date;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dima_d
 * 
 */
public class StudyImpLookInside extends Study {
	
	private static Logger LOG = LoggerFactory.getLogger(StudyImpLookInside.class);

	StudyImpLookInside(DicomObject dcmObj) {
		
		LOG.info(getDebugStr("DICOM Driver 'Look Inside'"));
		
		//инициализируем общие теги
		implCommon(dcmObj);
		
		
		SpecificCharacterSet cs = getCs();
		
		int tagPatientName = 0x34950022;
		int tagPatientBirthDate = 0x349500B2;
		int tagPatientSex = 0x34950042;
		int tagDoctor =  0x34970062;
		int tagOpeator = 0x34970052;
		int tagStudyType = 0x34970002;
		int tagStudyResult = 0x34970082;
		int tagStudyViewprotocol = 0x349700C2;
		
		int tagStudyDescriptionDateDate = 0x34970012;
		int tagStudyDescriptionDateTime = 0x34970022;
		
		
		

		// PatientName
		String cusomPatientName = null;
		DicomElement element = dcmObj.get(tagPatientName);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			cusomPatientName = element.getValueAsString(cs,
					element.length());
			cusomPatientName = cusomPatientName.replaceAll("\\^", " ");
			setPatientName(cusomPatientName);
		}
		
		
		
		// PatientBirthDate
		String cusomPatientBirthDate = null;
		element = dcmObj.get(tagPatientBirthDate);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			cusomPatientBirthDate = element.getValueAsString(cs,
					element.length());
			setPatientBirthDate(Date.valueOf(cusomPatientBirthDate));
		}
		
		
		
		// PatientSex
		String cusomPatientSex = null;
		element = dcmObj.get(tagPatientSex);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			cusomPatientSex = element.getValueAsString(cs,
					element.length());
			
			if(cusomPatientSex != null) {
				if (cusomPatientSex.length() > 1) {
					LOG.warn(getDebugStr("PATIENT_SEX to long [" + cusomPatientSex + "]"));
					cusomPatientSex = cusomPatientSex.substring(0, 1);
				}
				if (cusomPatientSex.equals("М")) {
					LOG.warn(getDebugStr("PATIENT_SEX bad type [" + cusomPatientSex + "]"));
					cusomPatientSex = "M";
				}
				if (cusomPatientSex.equals("Ж")) {
					LOG.warn(getDebugStr("PATIENT_SEX bad type [" + cusomPatientSex + "]"));
					cusomPatientSex = "F";
				}
			}
			setPatientSex(cusomPatientSex);
		}
		
		
		
		// PatientShortName (это КБП)
		setPatientShortName(Extractor.makeShortName(getPatientName(),
				getPatientBirthDate()));
		if (getPatientShortName() == null
				|| getPatientShortName().length() == 0) {
			setPatientShortName("notmuch");
		}
		
		
		// Doctor
		String customDoctor = null;
		element = dcmObj.get(tagDoctor);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			customDoctor = element.getValueAsString(cs,
					element.length());
			setStudyDoctor(customDoctor);
		}
		
		// Operator
		String customOperator = null;
		element = dcmObj.get(tagOpeator);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			customOperator = element.getValueAsString(cs,
					element.length());
			setStudyOperator(customOperator);
		}
		
		// StudyType
		String customStudyType = null;
		element = dcmObj.get(tagStudyType);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			customStudyType = element.getValueAsString(cs,
					element.length());
			setStudyType(customStudyType);
		}
		
		// StudyResult
		String customStudyResult = null;
		element = dcmObj.get(tagStudyResult);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			customStudyResult = element.getValueAsString(cs,
					element.length());
			setStudyResult(customStudyResult);
		}
		
		// StudyViewProtocol
		String customStudyViewProtocol = null;
		element = dcmObj.get(tagStudyViewprotocol);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			customStudyViewProtocol = element.getValueAsString(cs,
					element.length());
			setStudyViewProtocol(customStudyViewProtocol);
		}
		
		// PatientBirthDate
		String StudyDescriptionDate = null;
		String StudyDescriptionTime = null;
		
		
		
		element = dcmObj.get(tagStudyDescriptionDateDate);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			StudyDescriptionDate = element.getValueAsString(cs,
					element.length());
		}
		
		element = dcmObj.get(tagStudyDescriptionDateTime);
		if (element != null
				&& element.getValueAsString(cs, element.length()) != null) {
			StudyDescriptionTime = element.getValueAsString(cs,
					element.length());
		}
		
		if(StudyDescriptionDate != null && StudyDescriptionTime != null) {
			//TODO Дата описания = Дата проведеия исследования
//			System.out.println("!!! ["+StudyDescriptionDate + " " + StudyDescriptionTime+"]");
			setStudyViewProtocolDate(Date.valueOf(StudyDescriptionDate));
			setStudyDate(Date.valueOf(StudyDescriptionDate));
		}
		
		
		//TODO Костыли:
		//setStudyType("not implemented");
		
		
	}

}
