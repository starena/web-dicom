package org.psystems.dicom.commons.orm;

import java.util.ArrayList;
import java.util.Date;

import org.psystems.dicom.commons.IDataSource;

/**
 * @author dima_d Направление на исследование
 */
public class Direction {

	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private Employe doctorDirect;// Направивший врач
	private ArrayList<Diagnosis> diagnosisDirect;// Диагнозы при направлении
	private ArrayList<Service> servicesDirect;// Услуги при направлении
	private Date dateDirection;// Дата направления
	private ManufacturerDevice device;// Аппарат
	private Date datePlanned;// Планируемая дата выполнения исследования
	private Employe doctorPerformed;// Врач выполнивший исследование
	private String dirrectionCode;// Идентификатор случая заболевания
	private String dirrectionRoom;// Кабинет
	private ArrayList<Diagnosis> diagnosisPerformed;// Диагнозы при направлении
	private ArrayList<Service> servicesPerformed;// Выполненные услуги
	private Date datePerformed;// Дата выполнения исследования
	private Patient patient;
	private Date dateModified;// Планируемая дата выполнения исследования
	private Date dateRemoved;// Планируемая дата выполнения исследования

	public Direction getDirectionbyID(String directionId, IDataSource ds) {
		return null;

	}

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

	public Employe getDoctorDirect() {
		return doctorDirect;
	}

	public void setDoctorDirect(Employe doctorDirect) {
		this.doctorDirect = doctorDirect;
	}

	public ArrayList<Diagnosis> getDiagnosisDirect() {
		return diagnosisDirect;
	}

	public void setDiagnosisDirect(ArrayList<Diagnosis> diagnosisDirect) {
		this.diagnosisDirect = diagnosisDirect;
	}

	public ArrayList<Service> getServicesDirect() {
		return servicesDirect;
	}

	public void setServicesDirect(ArrayList<Service> servicesDirect) {
		this.servicesDirect = servicesDirect;
	}

	public Date getDateDirection() {
		return dateDirection;
	}

	public void setDateDirection(Date dateDirection) {
		this.dateDirection = dateDirection;
	}

	public ManufacturerDevice getDevice() {
		return device;
	}

	public void setDevice(ManufacturerDevice device) {
		this.device = device;
	}

	public Date getDatePlanned() {
		return datePlanned;
	}

	public void setDatePlanned(Date datePlanned) {
		this.datePlanned = datePlanned;
	}

	public Employe getDoctorPerformed() {
		return doctorPerformed;
	}

	public void setDoctorPerformed(Employe doctorPerformed) {
		this.doctorPerformed = doctorPerformed;
	}

	public String getDirrectionCode() {
		return dirrectionCode;
	}

	public void setDirrectionCode(String dirrectionCode) {
		this.dirrectionCode = dirrectionCode;
	}

	public String getDirrectionRoom() {
		return dirrectionRoom;
	}

	public void setDirrectionRoom(String dirrectionRoom) {
		this.dirrectionRoom = dirrectionRoom;
	}

	public ArrayList<Diagnosis> getDiagnosisPerformed() {
		return diagnosisPerformed;
	}

	public void setDiagnosisPerformed(ArrayList<Diagnosis> diagnosisPerformed) {
		this.diagnosisPerformed = diagnosisPerformed;
	}

	public ArrayList<Service> getServicesPerformed() {
		return servicesPerformed;
	}

	public void setServicesPerformed(ArrayList<Service> servicesPerformed) {
		this.servicesPerformed = servicesPerformed;
	}

	public Date getDatePerformed() {
		return datePerformed;
	}

	public void setDatePerformed(Date datePerformed) {
		this.datePerformed = datePerformed;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateRemoved() {
		return dateRemoved;
	}

	public void setDateRemoved(Date dateRemoved) {
		this.dateRemoved = dateRemoved;
	}

	@Override
	public String toString() {
		return "Direction [dateDirection=" + dateDirection + ", dateModified="
				+ dateModified + ", datePerformed=" + datePerformed
				+ ", datePlanned=" + datePlanned + ", dateRemoved="
				+ dateRemoved + ", device=" + device + ", diagnosisDirect="
				+ diagnosisDirect + ", diagnosisPerformed="
				+ diagnosisPerformed + ", directionId=" + directionId
				+ ", dirrectionCode=" + dirrectionCode + ", dirrectionRoom="
				+ dirrectionRoom + ", doctorDirect=" + doctorDirect
				+ ", doctorPerformed=" + doctorPerformed + ", id=" + id
				+ ", patient=" + patient + ", servicesDirect=" + servicesDirect
				+ ", servicesPerformed=" + servicesPerformed + "]";
	}

}
