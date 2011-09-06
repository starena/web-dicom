--CONNECT 'jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM;create=true';

--
-- Таблица "Направление"
--
CREATE TABLE WEBDICOM.DIRECTION (
	ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	DIRECTION_ID VARCHAR(512), -- штрих код (это StudyID)
	DIRECTION_CODE  VARCHAR(512), -- Идентификатор случая заболевания
	DIRECTION_DATE_PLANNED TIMESTAMP, -- Плановая дата и время выполнения исследования
	
	DEVICE  VARCHAR(512), -- Аппарат (STUDY_MANUFACTURER_MODEL_NAME)
	DEVICE_MODALITY VARCHAR(10),
	DIRECTION_LOCATION  VARCHAR(512), -- Кабинет
	
	DATE_DIRECTION DATE, -- Дата направления пациента (дата выписки направления)
	DOCTOR_DIRECT_NAME  VARCHAR(512), -- Имя врача который направил
	DOCTOR_DIRECT_CODE  VARCHAR(512), -- Код врача который направил
	
	DATE_PERFORMED DATE, -- Дата выполнения
	DOCTOR_PERFORMED_NAME  VARCHAR(512), -- врач который выполнил
	DOCTOR_PERFORMED_CODE  VARCHAR(512), -- Код врача который выполнил
	
	-- по пациенту
	PATIENT_ID VARCHAR(512), -- код пациента
	PATIENT_NAME VARCHAR(512) NOT NULL,-- ФИО пациента
	PATIENT_BIRTH_DATE DATE NOT NULL, -- д.р пациента
	PATIENT_SEX VARCHAR(1)  NOT NULL, -- пол пациента (M/F)
	PATIENT_SHORTNAME CHAR(7) NOT NULL, -- Это КБП. ФФФИОГГ
	DATE_MODIFIED TIMESTAMP NOT NULL, -- дата измения
	REMOVED TIMESTAMP, -- дата удаления
	CONSTRAINT WEBDICOM.PK_DIRECTION PRIMARY KEY (ID),
	UNIQUE (DIRECTION_ID)
);

--
-- Таблица диагнозов в "Направлении"
--
CREATE TABLE WEBDICOM.DIRECTION_DIAGNOSIS (
	FID_DIRECTION BIGINT, -- ссылка на исследование
	TYPE_ON_DIRECTION CHAR(1), -- Тип в направлении D|P (Dirrect Performed)
	DIAGNOSIS_CODE VARCHAR(50), -- Код (по МКБ)
	DIAGNOSIS_TYPE VARCHAR(100), -- Тип
	DIAGNOSIS_SUBTYPE VARCHAR(100), -- ПодтипТип
	DIAGNOSIS_DESCRIPTION VARCHAR(512), -- Описание
	CONSTRAINT WEBDICOM.FK_DIRECTION_DIAGNOSIS_DIRECTION FOREIGN KEY (FID_DIRECTION) REFERENCES WEBDICOM.DIRECTION (ID)
);

--
-- Таблица услуг в "Направлении"
--
CREATE TABLE WEBDICOM.DIRECTION_SERVICE (
	FID_DIRECTION BIGINT, -- ссылка на исследование
	TYPE_ON_DIRECTION CHAR(1), -- Тип в направлении D|P (Dirrect Performed)
	SERVICE_CODE VARCHAR(50), -- Код
	SERVICE_ALIAS VARCHAR(100), -- Алиас
	SERVICE_DESCRIPTION VARCHAR(512), -- Описание
	SERVICE_COUNT INT,
	CONSTRAINT WEBDICOM.FK_DIRECTION_SERVICE_DIRECTION FOREIGN KEY (FID_DIRECTION) REFERENCES WEBDICOM.DIRECTION (ID)
);

--
-- Таблица "исследование"
--
CREATE TABLE WEBDICOM.STUDY (
	ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	STUDY_UID VARCHAR(512) NOT NULL, 
	STUDY_ID VARCHAR(512), 
	STUDY_DATE DATE,
	STUDY_MODALITY VARCHAR(10), -- (0008,0060)
	STUDY_TYPE VARCHAR(512), 
	STUDY_DESCRIPTION  VARCHAR(512), --
	STUDY_DOCTOR  VARCHAR(512), -- Referring Physician's Name
	STUDY_OPERATOR  VARCHAR(512), -- Operators' Name
	STUDY_RESULT  VARCHAR(1024), --
	STUDY_VIEW_PROTOCOL  VARCHAR(1024), -- Протокол осмотра
	-- VIEW_PROTOCOL_BLOB  BLOB(1024), -- На будущее 
	STUDY_VIEW_PROTOCOL_DATE DATE, -- Дата осмотра
	STUDY_MANUFACTURER_MODEL_NAME VARCHAR(512),
	STUDY_MANUFACTURER_UID VARCHAR(512),
	PATIENT_ID VARCHAR(512),
	PATIENT_NAME VARCHAR(512) NOT NULL,
	PATIENT_SHORTNAME CHAR(7) NOT NULL, -- Это КБП. ФФФИОГГ
	PATIENT_BIRTH_DATE DATE NOT NULL,
	PATIENT_SEX VARCHAR(1)  NOT NULL,
	FID_DIRECTION BIGINT, -- ссылка на исследование
	DATE_MODIFIED TIMESTAMP NOT NULL, -- дата измения
	REMOVED TIMESTAMP, -- дата удаления
	CONSTRAINT WEBDICOM.PK_STUDY PRIMARY KEY (ID),
	CONSTRAINT WEBDICOM.FK_DIRECTION_STUDY FOREIGN KEY (FID_DIRECTION) REFERENCES WEBDICOM.DIRECTION (ID),
	UNIQUE (STUDY_UID)
);

CREATE INDEX WEBDICOM.STUDY_PATIENT_NAME ON WEBDICOM.STUDY (PATIENT_NAME);
CREATE INDEX WEBDICOM.STUDY_STUDY_DATE ON WEBDICOM.STUDY (STUDY_DATE);

--
-- Таблица атрибутов исследования
-- 
CREATE TABLE WEBDICOM.STUDY_ATTRIBUTE (
	FID_STUDY BIGINT NOT NULL,
	ATTRIBUTE INTEGER NOT NULL,
	VALUE_STRING VARCHAR(1024) NOT NULL,
	CONSTRAINT WEBDICOM.PK_STUDY_ATTRIBUTE PRIMARY KEY (FID_STUDY,ATTRIBUTE),
	CONSTRAINT WEBDICOM.FK_STUDY_ATTRIBUTE_STUDY FOREIGN KEY (FID_STUDY) REFERENCES WEBDICOM.STUDY (ID)	
);

--
-- Таблица DICOM-файл
--
CREATE TABLE WEBDICOM.DCMFILE (
	FID_STUDY BIGINT NOT NULL,
	ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	TYPE VARCHAR(512) NOT NULL, -- Тип файла
	DCM_FILE_NAME VARCHAR(512) NOT NULL, -- Полый путь к файлу
	NAME VARCHAR(512) NOT NULL, -- Имя файла (без дополнительного пути)
	DCM_FILE_SIZE BIGINT NOT NULL,
	MIME_TYPE VARCHAR(512), -- Тип контента (Null если нет)
	DOCUMENT_SIZE BIGINT, -- Размер вложенного документа
	IMAGE_FILE_SIZE BIGINT,
	IMAGE_WIDTH INTEGER, -- COLUMNS
	IMAGE_HEIGHT INTEGER, -- ROWS
	DATE_MODIFIED TIMESTAMP NOT NULL, -- дата измения
	REMOVED TIMESTAMP, -- дата удаления
	CONSTRAINT WEBDICOM.PK_DCMFILE PRIMARY KEY (ID),
	CONSTRAINT WEBDICOM.FK_DCMFILE_STUDY FOREIGN KEY (FID_STUDY) REFERENCES WEBDICOM.STUDY (ID),
	UNIQUE (DCM_FILE_NAME),
	UNIQUE (NAME)
);

--
-- Таблица ТЕГ DICOM-файла
-- 
CREATE TABLE WEBDICOM.DCMFILE_TAG (
	FID_DCMFILE BIGINT NOT NULL,
	TAG INTEGER NOT NULL,
	TAG_TYPE CHAR(2) NOT NULL,
	VALUE_STRING VARCHAR(1024) NOT NULL,
	CONSTRAINT WEBDICOM.PK_DCMFILE_TAG PRIMARY KEY (FID_DCMFILE,TAG),
	CONSTRAINT WEBDICOM.FK_DCMFILE_TAG_DCMFILE FOREIGN KEY (FID_DCMFILE) REFERENCES WEBDICOM.DCMFILE (ID)	
);



--
-- Статистические таблицы
--
CREATE TABLE WEBDICOM.STAT (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	METRIC_NAME VARCHAR(512) NOT NULL,
	METRIC_DATE DATE NOT NULL,
	METRIC_VALUE_LONG BIGINT,
	CONSTRAINT WEBDICOM.PK_STAT PRIMARY KEY (ID),
	UNIQUE (METRIC_NAME,METRIC_DATE)
	--INDEX WEBDICOM.PK_STAT1 (METRIC_VALUE_LONG)
);

CREATE TABLE WEBDICOM.DAYSTAT (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	METRIC_NAME VARCHAR(512) NOT NULL,
	METRIC_DATE DATE NOT NULL,
	METRIC_VALUE_LONG BIGINT,
	CONSTRAINT WEBDICOM.PK_DAYSTAT PRIMARY KEY (ID),
	UNIQUE (METRIC_NAME,METRIC_DATE)
	--INDEX (METRIC_VALUE_LONG)
);


EXIT;

