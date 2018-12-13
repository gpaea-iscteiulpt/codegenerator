package pt.iscte.pidesco.codegenerator.extensibility;

import java.util.HashMap;

public class UserCodeGenerator{
	
	private RangeScope rangeScope;
	private HashMap<String, String> macroToCode;
	
	public UserCodeGenerator(HashMap<String, String> macroToCode, RangeScope rangeScope) {
		this.macroToCode = macroToCode;
		this.rangeScope = rangeScope;
	}

	public RangeScope getRangeScope() {
		return rangeScope;
	}

	public void setRangeScope(RangeScope rangeScope) {
		this.rangeScope = rangeScope;
	}

	public HashMap<String, String> getMacroToCode() {
		return macroToCode;
	}

	public void setMacroToCode(HashMap<String, String> macroToCode) {
		this.macroToCode = macroToCode;
	}
	
	
}
