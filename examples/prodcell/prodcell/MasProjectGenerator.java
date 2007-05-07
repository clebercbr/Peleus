package prodcell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class to generate a number of Jason project files for testing the Production Cell
 * scenario. Maybe this class might be useful in the future if extended
 * @author meneguzz
 *
 */
public class MasProjectGenerator {
	protected List<JasonAgent> agents;
	protected String agentClassName;
	protected String environmentClassName;
	protected String environmentParameter;
	protected String environmentParExt;
	
	protected String targetDirectory;
	protected String projectName;
	
	protected int numberOfProjects;
	
	
	public static void main(String[] args) {
		MasProjectGenerator generator = new MasProjectGenerator(args);
		generator.generateProjects();
	}
	
	public MasProjectGenerator(String args[]) {
		this.agents = new ArrayList<JasonAgent>();
		this.agentClassName = null;
		this.environmentClassName = null;
		this.environmentParameter = null;
		this.environmentParExt = "";
		this.targetDirectory = null;
		this.numberOfProjects = 10;
		this.projectName = "dummy";
		this.parseArguments(args);
	}
	
	protected void parseArguments(String args[]) {
		//this.agents.add(new JasonAgent("prodcell"));
		//this.projectName = "prodcell";
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-project")) {
				if(++i < args.length) {
					this.projectName = args[i];
				} else {
					System.err.println("-project parameter requires a project name.");
				}
			} else if(args[i].equals("-agent")) {
				if(++i < args.length) {
					this.agents.add(new JasonAgent(args[i]));
				} else {
					System.err.println("-agent parameter requires an agent name.");
				}
			} else if(args[i].equals("-number-projects")) {
				if(++i < args.length) {
					try {
						int projects = Integer.parseInt(args[i]);
						this.numberOfProjects = projects;
					}catch (NumberFormatException e) {
						System.err.println("-number-projects parameter requires an integer");
					}
				} else {
					System.err.println("-number-projects parameter requires an integer");
				}
			} else if(args[i].equals("-o")) {
				if(++i < args.length) {
					File targetDir = new File(args[i]);
					if(targetDir.isDirectory() && targetDir.canWrite()) {
						this.targetDirectory = args[i];
					} else {
						System.err.println("Target file '"+args[i]+"' is not a directory or is not writeable");
					}
				} else {
					System.err.println("-o parameter requires a directory name");
				}
			} else if(args[i].equals("-environment")) {
				if(++i < args.length) {
					this.environmentClassName = args[i];
				} else {
					System.err.println("-environment parameter requires a class name");
				}
			} else if(args[i].equals("-envpar")) {
				if(++i < args.length) {
					this.environmentParameter = args[i];
				} else {
					System.err.println("-envpar parameter requires a parameter");
				}
			} else if(args[i].equals("-envpar-ext")) {
				if(++i < args.length) {
					this.environmentParExt = args[i];
				} else {
					System.err.println("-envpar-ext parameter requires a parameter");
				}
			} else {
				System.err.println("Unrecognized parameter '"+args[i]+"'");
			}
		}
	}
	
	public void generateProjects() {
		PrintWriter writer = null;
		for (int i = 0; i < numberOfProjects; i++) {
			int projectNumber = i+1;
			if(targetDirectory != null) {
				try {
					writer = new PrintWriter(
							new FileOutputStream(targetDirectory+"/"+projectName+projectNumber+".mas2j")
							);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(writer == null) {
				writer = new PrintWriter(System.out);
			}
			generateProject(projectNumber, writer);
			writer.flush();
		}
	}
	
	protected void generateProject(int number, PrintWriter writer) {
		writer.println("/* Jason Project                  */");
		writer.println("/* Generated by Project Generator */");
		writer.println("MAS "+projectName+number+" {");
		writer.println();
		if(environmentClassName != null) {
			writer.println("   environment:");
			writer.print("      "+environmentClassName);
			if(environmentParameter != null) {
				writer.println("(\""+environmentParameter+number+environmentParExt+"\")");
			} else {
				writer.println();
			}
		}
		writer.println("   agents:");
		for (JasonAgent agent : agents) {
			writer.print("      ");
			writer.print(agent.toString());
			writer.println(";");
		}
		writer.println("}");
	}
	
	protected class JasonAgent {
		public String agentName;
		
		public JasonAgent(String agentName) {
			this.agentName = agentName;
		}
		
		@Override
		public String toString() {
			return agentName;
		}
	}
}
