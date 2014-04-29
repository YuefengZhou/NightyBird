package dblayout;

import java.io.Serializable;
import java.util.Date;

public class SleepData implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date start, end;
	private int sdid;
	
	public SleepData() { // set default time here
		this.sdid = -1;
		this.start = new Date();
		this.end = new Date();
	}
	
	public SleepData(int sdid, Date start, Date end) {
		this.sdid = sdid;
		this.start = start;
		this.end = end;
	}
	
	public SleepData(Date start, Date end) {
		this.start = start;
		this.end = end;
		this.sdid = -1;
	}
	
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public int getSdid() {
		return sdid;
	}
	public void setSdid(int sdid) {
		this.sdid = sdid;
	}
}
