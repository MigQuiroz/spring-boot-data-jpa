/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bolsadeideas.springboot.app.models.entity;

/**
 *
 * @author APerez
 */
public class ModelGetONTDto {
    	
   private int frame;           
   private int slot;            
   private int port;            
   private String olt;          
   private int ontID;           
   private String ipolt;           
   private String etiqueta;     
   private String sn;
   
   private int typeCut; 
   private String nameProcess; 

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

   

    public String getOlt() {
		return olt;
	}

	public void setOlt(String olt) {
		this.olt = olt;
	}

	public int getOntID() {
        return ontID;
    }

    public void setOntID(int ontID) {
        this.ontID = ontID;
    }

    

    public String getIpolt() {
		return ipolt;
	}

	public void setIpolt(String ipolt) {
		this.ipolt = ipolt;
	}

	public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

   

    public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getTypeCut() {
        return typeCut;
    }

    public void setTypeCut(int typeCut) {
        this.typeCut = typeCut;
    }

    public String getNameProcess() {
        return nameProcess;
    }

    public void setNameProcess(String nameProcess) {
        this.nameProcess = nameProcess;
    }
   
   
   
   
}
