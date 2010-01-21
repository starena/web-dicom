
CREATE TABLE webdicom.dcmfile (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	DCM_FILE_NAME VARCHAR(512) NOT NULL,
	PATIENT_BIRTH_DATE DATE NOT NULL,
	PATIENT_NAME VARCHAR(512) NOT NULL,
	STUDY_DATE DATE NOT NULL,
	CONSTRAINT primary_key PRIMARY KEY (ID),
	UNIQUE (DCM_FILE_NAME)
)

CREATE TABLE webdicom.study (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	FID INTEGER NOT NULL,	
	DCM_FILE_NAME VARCHAR(512) NOT NULL,
	PATIENT_BIRTH_DATE DATE NOT NULL,
	PATIENT_NAME VARCHAR(512) NOT NULL,
	STUDY_DATE DATE NOT NULL,
	CONSTRAINT study_primary_key PRIMARY KEY (ID),
	CONSTRAINT FLTS_FK FOREIGN KEY (FID) REFERENCES webdicom.dcmfile (ID),
	UNIQUE (DCM_FILE_NAME)
)

CREATE TABLE webdicom.series (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	FID INTEGER NOT NULL,	
	DCM_FILE_NAME VARCHAR(512) NOT NULL,
	PATIENT_BIRTH_DATE DATE NOT NULL,
	PATIENT_NAME VARCHAR(512) NOT NULL,
	STUDY_DATE DATE NOT NULL,
	CONSTRAINT study_primary_key PRIMARY KEY (ID),
	CONSTRAINT FLTS_FK FOREIGN KEY (FID) REFERENCES webdicom.dcmfile (ID),
	UNIQUE (DCM_FILE_NAME)
)

CREATE TABLE webdicom.image (
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	FID INTEGER NOT NULL,	
	DCM_FILE_NAME VARCHAR(512) NOT NULL,
	PATIENT_BIRTH_DATE DATE NOT NULL,
	PATIENT_NAME VARCHAR(512) NOT NULL,
	STUDY_DATE DATE NOT NULL,
	CONSTRAINT study_primary_key PRIMARY KEY (ID),
	CONSTRAINT FLTS_FK FOREIGN KEY (FID) REFERENCES webdicom.dcmfile (ID),
	UNIQUE (DCM_FILE_NAME)
) 
