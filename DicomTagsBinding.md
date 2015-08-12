Physician - врач

Requesting - запрашивающий

Requested - запрошенный

Performing - выполнение (исполнительный)

Scheduled - плановый

Procedure - процедура (исследование, обследование , фактически это направление, направление на обследование)

Location - размещение (где проводится исследование)

!!!1 Статус исследования (выполнен или нет)!!!!

# Поля для создание направления: #

| **Наименование** | **Тег** | **Dicom-расшифровка** | **Поле в БД** |
|:-----------------|:--------|:----------------------|:--------------|
| Номер направления (штрих-код) | Tag.StudyID | (0020,0010) VR=SH, VM=1 Study ID | STUDY.STUDY\_ID |
| **Пациент**      |         |                       |               |
| Код пациента     | Tag.PatientID | (0010,0020) VR=LO, VM=1 Patient ID | STUDY.PATIENT\_ID |
| ФИО пациента     | Tag.PatientName | (0010,0010) VR=PN, VM=1 Patient's Name | STUDY.PATIENT\_NAME |
| Дата рождения пациента | Tag.PatientBirthDate | (0010,0030) VR=DA, VM=1 Patient's Birth Date  | STUDY.PATIENT\_BIRTH\_DATE |
| Пол пациента     | Tag.PatientSex | (0010,0040) VR=CS, VM=1 Patient's Sex | STUDY.PATIENT\_SEX |


| **Направивший  Врач** | | | | |
|:----------------------|:|:|:|:|
| Имя                   | Tag.ReferringPhysicianName | (0008,0090) VR=PN, VM=1 Referring Physician's Name | STUDY.STUDY\_DOCTOR\_REQUEST | формат: "ФИО {ТАБЕЛЬНЫЙ}" |
| Направивший  Врач **TODO Удалить?** | Tag.RequestingPhysician | (0032,1032) VR=PN, VM=1 Requesting Physician | STUDY.STUDY\_DOCTOR\_REQUEST | формат: "ФИО {ТАБЕЛЬНЫЙ}" |




| **Врач выполнивший исследование** | | | | |
|:----------------------------------|:|:|:|:|
| ФИО                               | Tag.PerformingPhysicianName| ((0008,1050) VR=PN, VM=1-n Performing Physician's Name  | STUDY.STUDY\_DOCTOR\_PERFORM | формат: "ФИО {ТАБЕЛЬНЫЙ}" |
| Место выполнения                  | Tag.PerformedLocation | (0040,0243) VR=SH, VM=1 Performed Location | STUDY.STUDY\_LOCATION | формат: "КОРПУС-КАБИНЕТ" |

| **Исследование** | | |
|:-----------------|:|:|
| Запрашиваемое исследование(описание) | Tag.RequestedProcedureDescription | (0032,1060) VR=LO, VM=1 Requested Procedure Description |
| ID запрашиваемого исследования (штрих код? **TODO а как же StudyID?**) | Tag.RequestedProcedureID | (0040,1001) VR=SH, VM=1 Requested Procedure ID |
| Код запрашиваемого исследования| Tag.RequestedProcedureCodeSequence | (0032,1064) VR=SQ, VM=1 Requested Procedure Code Sequence **TODO Вариант2**  |
| Место выполнения исследования | Tag.RequestedProcedureLocation | (0040,1005) VR=LO, VM=1 Requested Procedure Location |
| Комментарий исследования | Tag.RequestedProcedureComments | (0040,1400) VR=LT, VM=1 Requested Procedure Comments |

| Аттрибуты запроса | Tag.RequestAttributesSequence | (0040,0275) VR=SQ, VM=1 Request Attributes Sequence |
|:------------------|:------------------------------|:----------------------------------------------------|
| AE запрашиваемой процедуры | Tag.RequestingAE              | (0074,1236) VR=AE, VM=1 Requesting AE **TODO Использовать как идентификатор апарата?** |
| Запрашиваемая услуга | Tag.RequestingService         | (0032,1033) VR=LO, VM=1 Requesting Service          |
| Имя аппарата      | Tag.StationName               | (0008,1010) VR=SH, VM=1 Station Name [дока](http://dicomlookup.com/lookup.asp?sw=Ttable&q=C.7-8&Submit=Display) |

| Дата начала выполнения процедуры | Tag.PerformedProcedureStepStartDate | (0040,0244) VR=DA, VM=1 Performed Procedure Step Start Date |
|:---------------------------------|:------------------------------------|:------------------------------------------------------------|
| Дата окончания выполнения исследования | Tag.PerformedProcedureStepEndDate   | (0040,0250) VR=DA, VM=1 Performed Procedure Step End Date   |





| Плановая дата начала исследования | Tag.ScheduledProcedureStepStartDate | 0040,0002) VR=DA, VM=1 Scheduled Procedure Step Start Date |
|:----------------------------------|:------------------------------------|:-----------------------------------------------------------|
| Выполненная дата окончания исследования | Tag.PerformedProcedureStepEndDate   | (0040,0250) VR=DA, VM=1 Performed Procedure Step End Date  |

Массив услуг "код,наименование,синоним(это наименование исследования)"


Диагноз
| Приемный диагноз | Tag.AdmittingDiagnosesDescription | (0008,1080) VR=LO, VM=1-n Admitting Diagnoses Description |
|:-----------------|:----------------------------------|:----------------------------------------------------------|
| приемные диагнозы | Tag.AdmittingDiagnosesCodeSequence | (0008,1084) VR=SQ, VM=1 Admitting Diagnoses Code Sequence |
| Окончательный диагноз | Tag.DischargeDiagnosisDescription | (0038,0040) VR=LO, VM=1 Discharge Diagnosis Description RET |
| Окончательные диагнозы | Tag.DischargeDiagnosisCodeSequence | (0038,0044) VR=SQ, VM=1 Discharge Diagnosis Code Sequence RET |

Дата выписки направления

Идентификатор аппарата

Плановая дата + время выполнения

Идентификатор случая заболевания

# Поля для получения результатов направления: #

StudyID (штрих-код)

Кабинет

Идентификатор случая заболевания

Массив оказанных услуг "код,наименование,синоним(это наименование исследования)"

PatientID

PatientName (ФИО пациента + дата рождения + пол)

Код направившего врача

ФИО направившего врача

Код врача выполнившего исследование

ФИО врача выполнившего исследование

Диагноз (диагнозы?)

Дата выполнения