package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.Arrays;

public class DirectionProxy implements Serializable {

    private static final long serialVersionUID = 2203407311209901623L;

    private Long id; // Внутренний ID
    private String directionId; // штрих код
    private String modality;// модальность
    private EmployeeProxy doctorDirect;// Направивший врач
    private DiagnosisProxy[] diagnosisDirect;// Диагнозы при направлении
    private ServiceProxy[] servicesDirect;// Услуги при направлении
    private String dateDirection;// Дата направления. формат "yyyy-mm-dd"
    private ManufacturerDeviceProxy device;// Аппарат
    private String dateTimePlanned;// Планируемая дата выполнения исследования.
				   // формат "yyyy-mm-dd hh:mm:ss"
    private EmployeeProxy doctorPerformed;// Врач выполнивший исследование
    private String directionCode;// Идентификатор случая заболевания
    private String directionLocation;// Кабинет
    private DiagnosisProxy[] diagnosisPerformed;// Диагнозы после выполнения
						// исследования
    private ServiceProxy[] servicesPerformed;// Выполненные услуги
    private String datePerformed;// Дата выполнения исследования. формат
				 // "yyyy-mm-dd"
    private PatientProxy patient; // Пациент
    private String dateTimeModified;// Дата модификации. формат
				    // "yyyy-mm-dd hh:mm:ss"
    private String dateTimeRemoved;// Дата удаления. формат
				   // "yyyy-mm-dd hh:mm:ss"

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getDirectionId() {
	return directionId;
    }

    public void setDirectionId(String directionId) {
	this.directionId = directionId;
    }

    public String getModality() {
	return modality;
    }

    public void setModality(String modality) {
	this.modality = modality;
    }

    public EmployeeProxy getDoctorDirect() {
	return doctorDirect;
    }

    public void setDoctorDirect(EmployeeProxy doctorDirect) {
	this.doctorDirect = doctorDirect;
    }

    public DiagnosisProxy[] getDiagnosisDirect() {
	return diagnosisDirect;
    }

    public void setDiagnosisDirect(DiagnosisProxy[] diagnosisDirect) {
	this.diagnosisDirect = diagnosisDirect;
    }

    public ServiceProxy[] getServicesDirect() {
	return servicesDirect;
    }

    public void setServicesDirect(ServiceProxy[] servicesDirect) {
	this.servicesDirect = servicesDirect;
    }

    public String getDateDirection() {
	return dateDirection;
    }

    public void setDateDirection(String dateDirection) {
	this.dateDirection = dateDirection;
    }

    public ManufacturerDeviceProxy getDevice() {
	return device;
    }

    public void setDevice(ManufacturerDeviceProxy device) {
	this.device = device;
    }

    public String getDateTimePlanned() {
	return dateTimePlanned;
    }

    public void setDateTimePlanned(String datePlanned) {
	this.dateTimePlanned = datePlanned;
    }

    public EmployeeProxy getDoctorPerformed() {
	return doctorPerformed;
    }

    public void setDoctorPerformed(EmployeeProxy doctorPerformed) {
	this.doctorPerformed = doctorPerformed;
    }

    public String getDirectionCode() {
	return directionCode;
    }

    public void setDirectionCode(String directionCode) {
	this.directionCode = directionCode;
    }

    public String getDirectionLocation() {
	return directionLocation;
    }

    public void setDirectionLocation(String directionLocation) {
	this.directionLocation = directionLocation;
    }

    public DiagnosisProxy[] getDiagnosisPerformed() {
	return diagnosisPerformed;
    }

    public void setDiagnosisPerformed(DiagnosisProxy[] diagnosisPerformed) {
	this.diagnosisPerformed = diagnosisPerformed;
    }

    public ServiceProxy[] getServicesPerformed() {
	return servicesPerformed;
    }

    public void setServicesPerformed(ServiceProxy[] servicesPerformed) {
	this.servicesPerformed = servicesPerformed;
    }

    public String getDatePerformed() {
	return datePerformed;
    }

    public void setDatePerformed(String datePerformed) {
	this.datePerformed = datePerformed;
    }

    public PatientProxy getPatient() {
	return patient;
    }

    public void setPatient(PatientProxy patient) {
	this.patient = patient;
    }

    public String getDateTimeModified() {
	return dateTimeModified;
    }

    public void setDateTimeModified(String dateTimeModified) {
	this.dateTimeModified = dateTimeModified;
    }

    public String getDateTimeRemoved() {
	return dateTimeRemoved;
    }

    public void setDateTimeRemoved(String dateTimeRemoved) {
	this.dateTimeRemoved = dateTimeRemoved;
    }

    @Override
    public String toString() {
	return "DirectionProxy [id=" + id + ", directionId=" + directionId + ", modality=" + modality
		+ ", doctorDirect=" + doctorDirect + ", diagnosisDirect=" + Arrays.toString(diagnosisDirect)
		+ ", servicesDirect=" + Arrays.toString(servicesDirect) + ", dateDirection=" + dateDirection
		+ ", device=" + device + ", dateTimePlanned=" + dateTimePlanned + ", doctorPerformed="
		+ doctorPerformed + ", directionCode=" + directionCode + ", directionLocation=" + directionLocation
		+ ", diagnosisPerformed=" + Arrays.toString(diagnosisPerformed) + ", servicesPerformed="
		+ Arrays.toString(servicesPerformed) + ", datePerformed=" + datePerformed + ", patient=" + patient
		+ ", dateTimeModified=" + dateTimeModified + ", dateTimeRemoved=" + dateTimeRemoved + "]";
    }

}
