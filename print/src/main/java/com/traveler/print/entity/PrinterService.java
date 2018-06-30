package com.traveler.print.entity;

import javax.print.PrintService;

import lombok.Data;
@Data
public class PrinterService {
	private PrintService printService;
	private boolean isDefault=false;
}
