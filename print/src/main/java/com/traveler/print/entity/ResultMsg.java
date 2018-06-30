package com.traveler.print.entity;
import lombok.Data;
@Data
public class ResultMsg {
	private boolean flag=false;
	private String msg="系统未知异常！";
	private Object obj;
}
