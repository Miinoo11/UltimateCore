package dev.miinoo.ucore.command;

public final class CommandDescription {

	private String description;
	private String syntax;
	private String syntaxPermission;

	public CommandDescription(String description, String syntax, String syntaxPermission) {
		this.description = description;
		this.syntax = syntax;
		this.syntaxPermission = syntaxPermission;
	}
	
	public CommandDescription(String description, String syntax) {
		this.description = description;
		this.syntax = syntax;
	}
	
	public CommandDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSyntax() {
		return syntax;
	}
	
	public String getSyntaxPermission() {
		return syntaxPermission;
	}
	
}
