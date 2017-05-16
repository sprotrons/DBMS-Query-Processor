//Generates "MFStruct.java" file

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeMF
{
	//Array lists
	private ArrayList<PairingTool<String, String>> FirstAllTypeName = null;
	private ArrayList<PairingTool<String, String>> FirstGroupingTypeName = null;
	private ArrayList<PairingTool<String, String>> FirstVFVar = null;
	private ArrayList<String> FirstOrigFunctions = new ArrayList<String>();
	private ArrayList<PairingTool<Integer, String>> FirstFunctionNumberNames = 
			new ArrayList<PairingTool<Integer, String>>();
	private String structStr;
	private String classStr;
	private String strInitFunction;

	//Constructor
	public MakeMF()
	{
		structStr = null;
		classStr = null;
		strInitFunction = null;
	}

	//Override Constructor
	public MakeMF(MakeMF file)
	{
		this.FirstAllTypeName = file.FirstAllTypeName;
		this.FirstGroupingTypeName = file.FirstGroupingTypeName;
		this.FirstVFVar = file.FirstVFVar;
		this.FirstOrigFunctions = file.FirstOrigFunctions;
		this.FirstFunctionNumberNames = file.FirstFunctionNumberNames;
		this.structStr = file.structStr;
		this.classStr = file.classStr;
		this.strInitFunction = file.strInitFunction;
	}
	
	//get function list
	public ArrayList<PairingTool<Integer, String>> getFuncitonList()
	{
		return this.FirstFunctionNumberNames;
	}
	
	//get list of grouping type names
	public ArrayList<PairingTool<String, String>> getGroupingTypeNameList()
	{
		return this.FirstGroupingTypeName;
	}
	
	
	//get strInitFunction
	public String getInitFunctionString()
	{
		return this.strInitFunction;
	}
	
	//return list of all type names
	public ArrayList<PairingTool<String, String>> getAllTypeNameList()
	{
		return this.FirstAllTypeName;
	}
	
	//Struct String
	public void setStructString(FileToArray fileToArray, DBMeta info)
	{
		structStr = "struct mf_struct\n{\n";
		String columnName = null;
		for(int i = 0; i != fileToArray.FirstSelectAttr.size(); i++)
		{
			structStr += "\t";
			String columnOrig = fileToArray.FirstSelectAttr.get(i);
			columnName = getColumnName(columnOrig);
			String type = info.getTypeFromColumn(columnName);
			structStr += type;
			structStr += " ";
			structStr += convertVariableName(columnOrig);
			if(columnOrig.equals("cust"))
			{
				structStr += "[10]";
			}
			structStr += ";\n";
		}
		structStr += "};\n";
	}

	//set JAVA Class String
	public void setClassString(FileToArray mforig, DBMeta info)
	{
		//get list of function vects
		for(int i = 0; i != mforig.FirstFV.size(); i++)
		{
			if(mforig.FirstFV.get(i).contains("avg") && FirstOrigFunctions.contains("avg") == false)
			{
				FirstOrigFunctions.add("avg");
			}
			if(mforig.FirstFV.get(i).contains("max") && FirstOrigFunctions.contains("max") == false)
			{
				FirstOrigFunctions.add("max");
			}
			if(mforig.FirstFV.get(i).contains("count") && FirstOrigFunctions.contains("count") == false)
			{
				FirstOrigFunctions.add("count");
			}
			if(mforig.FirstFV.get(i).contains("min") && FirstOrigFunctions.contains("min") == false)
			{
				FirstOrigFunctions.add("min");
			}
			if(mforig.FirstFV.get(i).contains("sum") && FirstOrigFunctions.contains("sum") == false)
			{
				FirstOrigFunctions.add("sum");
			}
		}

		FirstAllTypeName = new ArrayList<PairingTool<String, String>>();
		//Add top of file comments
		
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

		classStr = "//This code is created from MakeMF.java.\n";
		classStr += "//Generated Time:" + ft.format(dNow) + ".\n";
		classStr += "//\n";
		classStr += "//This implements the methods like avg, sum, max...\n\n";
		classStr += "public class ";
		classStr += "CreatedMF\n{\n";
		classStr += "\t//all variables...\n";
		String columnOrig = null;

		for(int i = 0; i != mforig.FirstSelectAttr.size(); i++)
		{
			columnOrig = mforig.FirstSelectAttr.get(i);
			String columnName = getColumnName(columnOrig);
			if(columnOrig.contains("count") == true)
			{
				continue;
			}
			String type = info.getTypeFromColumn(columnName);
			PairingTool<String, String> pairTypeName = new PairingTool<String, String>(type, 
					convertVariableName(columnOrig));
			if(this.FirstAllTypeName.contains(new PairingTool<String, String>(type,
					convertVariableName(columnOrig))) == true)
			{
				continue;
			}
			classStr += "\t";
			classStr += type;
			classStr += " ";
			classStr += convertVariableName(columnOrig);
			classStr += ";\n";
			FirstAllTypeName.add(pairTypeName);
			//Add sum_variables if not exist
			if(columnOrig.contains("max")||
					columnOrig.contains("avg")||columnOrig.contains("min")||
					columnOrig.contains("count") && columnOrig.contains("sum") == false)
			{
				String subName = this.mySumSubString(columnOrig);
				PairingTool<String, String> pairTypeName0 = new PairingTool<String, String>(type, "sum_" + 
						subName + "_" + this.getFunctionNameFirstNumber(columnOrig));
				if(this.FirstAllTypeName.contains(pairTypeName0) == true)
				{
					continue;
				}
				else
				{
					classStr += "\t" + type + " sum_" + subName;
					classStr += "_" + this.getFunctionNameFirstNumber(columnOrig) + ";\n";
					FirstAllTypeName.add(pairTypeName0);
				}
			}
		}

		//Add count_variables
		for(int i = 0; i != mforig.numGroupingVari; i++)
		{
			classStr = classStr + "\tint count_" + Integer.toString(i+1) + ";\n";
			PairingTool<String, String> pairTypeName = new PairingTool<String, String>("int", "count_" + 
					Integer.toString(i+1));
			FirstAllTypeName.add(pairTypeName);
		}

		this.setInitVar(mforig, info);
		this.setGroupingTypeName(mforig, info);
		classStr = this.addJavaInitFunction(mforig, info, classStr);
		classStr = this.addJavaEqualsFuction(classStr);
		//Add sum function(s)
		if(FirstOrigFunctions.contains("sum"))
		{
			classStr = this.addJavaSumFunction(classStr);
		}
		//Add avg function(s)
		if(FirstOrigFunctions.contains("avg"))
		{
			classStr = this.addJavaAvgFunction(classStr);
		}
		//Add max function(s) and min function(s)
		if(FirstOrigFunctions.contains("max"))
		{
			classStr = this.addJavaMaxFunction(classStr);
		}
		if(FirstOrigFunctions.contains("min"))
		{
			classStr = this.addJavaMinFunction(classStr);
		}
		
		//Add all set_count_* functions...
		for(int i = 0; i != mforig.numGroupingVari; i++)
		{
			classStr += "\n\tpublic void ";
			String functionName = "set_count_" + new Integer(i+1);
			classStr += functionName + "()\n";
			classStr += "\t{\n";
			classStr += "\t\tcount_" + new Integer(i+1) + "++;\n";
			classStr += "\t}\n";
		}
		classStr += "\n}\n";
	}

	public String getStructStr()
	{
		return structStr;
	}

	public String getClassStr()
	{
		return classStr;
	}

	private String getColumnName(String str)
	{
		int j = 0;
		for(int i = str.length() - 1; i != 0; i--)
		{
			if(str.charAt(i) == '_')
			{
				j = i;
				break;
			}
			if(i == 1 && str.charAt(0) != '_')
			{
				return str;
			}
		}
		str = str.substring(j + 1, str.length());
		return str;
	}

	//Convert from 1_avg_length to avg_length_1
	public String convertVariableName(String name)
	{
		String numberStr = null;
		if(Character.isDigit(name.charAt(0)) )
		{
			int j = 0;
			for(int i = 0; i != name.length(); i++)
			{
				if(name.charAt(i) == '_')
				{
					j = i;
					break;
				}
			}
			numberStr = name.substring(0, j);
			name = name.substring(j + 1, name.length());
		}
		if(numberStr != null)
		{
			name += "_";
			name += numberStr;
		}
		return name;
	}

	private String mySumSubString(String name)
	{
		String sumColumnNameStr = null;
		if(Character.isDigit(name.charAt(0)))
		{
			int j = 0;
			for(int i = name.length(); i != 0; i--)
			{
				if(name.charAt(i - 1) == '_')
				{
					j = i;
					break;
				}
			}
			sumColumnNameStr = name.substring(j, name.length());
		}
		return sumColumnNameStr;
	}

	//Generate the initialization functions
	private String addJavaInitFunction(FileToArray mforig, DBMeta info, String classStr)
	{
		classStr += "\n\t//initialize the variables in each group.";
		for(int j = 0; j != mforig.numGroupingVari; j++)
		{
			classStr += "\n\tpublic void initialization_" + new Integer(j+1) + "(";
			this.strInitFunction = new String();
			for(int i = 0; i != FirstGroupingTypeName.size(); i++)
			{
				String type = FirstGroupingTypeName.get(i).getFirst();
				String name = FirstGroupingTypeName.get(i).getSecond();
				this.strInitFunction += name + "Tmp" + ",";
				classStr += type;
				classStr += " ";
				classStr += name;
				classStr += "Tmp";
				classStr += ",";
			}
			for(int i = 0; i != FirstVFVar.size(); i++)
			{
				String type = FirstVFVar.get(i).getFirst();
				String name = FirstVFVar.get(i).getSecond();
				this.strInitFunction += name + "Tmp";
				classStr = classStr + type + " " + name;
				if(i != FirstVFVar.size() - 1)
				{
					this.strInitFunction += ",";
					classStr += ",";
				}
			}
			this.strInitFunction += ");\n";
			classStr += ")\n\t{\n";
			for(int i = 0; i != FirstAllTypeName.size(); i++)
			{
				String varName = FirstAllTypeName.get(i).getSecond();
				if(Character.isDigit(varName.charAt(varName.length() - 1)) == false
						|| this.getVariableNumber(varName) == j + 1)
				{
					classStr += "\t\t";
					classStr += varName;
					classStr += " = ";
					String type = info.getTypeFromColumn(varName);
					if(FirstGroupingTypeName.contains(new PairingTool<String, String>(type, varName)) == true)
					{
						classStr += varName;
						classStr += "Tmp";
					}
					type = info.getTypeFromColumn(myInitSubString(varName));
					if(FirstVFVar.contains(new PairingTool<String, String>(type,
							this.myInitSubString(varName))) == true)
					{
						classStr += this.myInitSubString(varName);
					}
					if(varName.contains("count"))
					{
						classStr += "1";
					}
					classStr += ";\n";	
				}
			}
			classStr += "\t}\n";
		}
		return classStr;
	}

	private void setInitVar(FileToArray mforig, DBMeta info)
	{
		FirstVFVar = new ArrayList<PairingTool<String, String>>();
		for(int i = 0; i != mforig.FirstFV.size(); i ++)
		{
			int i0 = 0;
			if(Character.isDigit(mforig.FirstFV.get(i).charAt(0)))
			{
				String tmp = mforig.FirstFV.get(i);
				for(int j = tmp.length(); j != 0; j--)
				{
					if(tmp.charAt(j - 1) == '_')
					{
						i0 = j;
						break;
					}
				}
				String name = tmp.substring(i0, tmp.length());
				String type = info.getTypeFromColumn(name);
				PairingTool<String, String> pairTypeName = new PairingTool<String, String>(type, name);
				if(FirstVFVar.contains(pairTypeName) == false)
				{
					FirstVFVar.add(pairTypeName);
				}
			}
		}
	}

	private void setGroupingTypeName(FileToArray mforig, DBMeta info)
	{
		FirstGroupingTypeName = new ArrayList<PairingTool<String, String>>();
		for(int i = 0; i != mforig.FirstGroupingAttr.size(); i++)
		{
			String name = mforig.FirstGroupingAttr.get(i);
			String type = info.getTypeFromColumn(name);
			PairingTool<String, String> pairNameType = new PairingTool<String, String>(type, name);
			if(FirstGroupingTypeName.contains(pairNameType) == false)
			{
				FirstGroupingTypeName.add(pairNameType);
			}
		}
	}

	//cut out the avg_ or sum_ only really variable names are left.
	//avg_length_1 => length
	private String myInitSubString(String varName)
	{
		String tmpName = null;
		int j = 0;
		for(int i = 0; i != varName.length(); i++)
		{
			if(varName.charAt(i) == '_' && j == 0)
			{
				j = i;
				continue;
			}
			if(varName.charAt(i) == '_' && j != 0)
			{
				tmpName = varName.substring(j + 1, i);
				break;
			}
		}
		return tmpName;
	}

	//Generate equals() function.
	private String addJavaEqualsFuction(String classStr)
	{
		String tmpStr = classStr;
		tmpStr += "\n\t//equals() fucntion.\n";
		tmpStr += "\tpublic boolean equals(";
		for(int i = 0; i != this.FirstGroupingTypeName.size(); i++)
		{
			tmpStr += this.FirstGroupingTypeName.get(i).getFirst();
			tmpStr += " ";
			tmpStr += this.FirstGroupingTypeName.get(i).getSecond();
			tmpStr += "Tmp";
			if(i != this.FirstGroupingTypeName.size() - 1)
			{
				tmpStr += ",";
			}
		}
		tmpStr += ")\n\t{\n";
		tmpStr += "\t\tif(";
		for(int i = 0; i != this.FirstGroupingTypeName.size(); i++)
		{
			tmpStr += this.FirstGroupingTypeName.get(i).getSecond();
			tmpStr += ".equals(";
			tmpStr += this.FirstGroupingTypeName.get(i).getSecond();
			tmpStr += "Tmp)";
			if(i != this.FirstGroupingTypeName.size() - 1)
			{
				tmpStr += " && ";
			}
		}
		tmpStr += ")\n\t\t{\n";
		tmpStr += "\t\t\treturn true;\n";
		tmpStr += "\t\t}\n";
		tmpStr += "\t\telse\n\t\t{\n";
		tmpStr += "\t\t\treturn false;\n";
		tmpStr += "\t\t}\n\t}\n";
		return tmpStr;
	}

	//Get variable number
	//avg_length_1 => 1
	private int getVariableNumber(String name)
	{
		int ireturn = 0;
		for(int i = name.length(); i != 0 ; i--)
		{
			if(name.charAt(i - 1) == '_')
			{
				String sub = name.substring(i, name.length());
				ireturn = Integer.parseInt(sub);
				break;
			}
			else
			{
				continue;
			}
		}
		if(ireturn == 0)
		{
			System.out.println("Variable Num Error");
		}
		return ireturn;
	}

	//Add avg Functions
	private String addJavaAvgFunction(String classStr)
	{
		classStr += "\n\t//average functions.\n";
		for(int i = 0; i != this.FirstAllTypeName.size(); i++)
		{
			if(this.FirstAllTypeName.get(i).getSecond().contains("avg"))
			{
				String name = this.FirstAllTypeName.get(i).getSecond();
				String type = this.FirstAllTypeName.get(i).getFirst();
				classStr += "\tpublic void ";
				String functionName = "set_" + name;
				classStr += functionName;
				this.FirstFunctionNumberNames.add(new PairingTool<Integer, String>(
						new Integer(this.getVariableNumber(functionName)), functionName));
				classStr += "(";
				classStr += type;
				classStr += " ";
				classStr += this.myInitSubString(name);
				classStr += "Tmp)\n";
				String shortName = this.myInitSubString(name);
				classStr += "\t{\n";
				classStr += "\t\t" + name + " = " + "sum_" + shortName + "_"
							+ this.getVariableNumber(name) + 
						" / count_" + this.getVariableNumber(name) + ";\n";
				classStr += "\t}\n";
			}
		}
		return classStr;
	}

	//Generate sum functs
	private String addJavaSumFunction(String classStr)
	{
		classStr += "\n\t//sum functions.\n";
		for(int i = 0; i != this.FirstAllTypeName.size(); i++)
		{
			if(this.FirstAllTypeName.get(i).getSecond().contains("sum"))
			{
				String name = this.FirstAllTypeName.get(i).getSecond();
				String type = this.FirstAllTypeName.get(i).getFirst();
				classStr += "\tpublic void ";
				String functionName = "set_" + name;
				this.FirstFunctionNumberNames.add(new PairingTool<Integer, String>(
						new Integer(this.getVariableNumber(functionName)), functionName));
				classStr += functionName;
				classStr += "(" + type + " " + this.myInitSubString(name) + "Tmp)\n";
				String shortName = this.myInitSubString(name);
				classStr += "\t{\n";
				classStr += "\t\tsum_" + shortName + "_" + this.getVariableNumber(name) + "+="+
						shortName + "Tmp;\n";
				classStr += "\t}\n";
			}
		}
		return classStr;
	}
	
	//Generate maxFunctions
	private String addJavaMaxFunction(String classStr)
	{
		classStr += "\n\t//max functions\n";
		for(int i = 0; i != this.FirstAllTypeName.size(); i++)
		{
			if(this.FirstAllTypeName.get(i).getSecond().contains("max"))
			{
				String name = this.FirstAllTypeName.get(i).getSecond();
				String type = this.FirstAllTypeName.get(i).getFirst();
				classStr += "\tpublic void ";
				String functionName = "set_" + name;
				this.FirstFunctionNumberNames.add(new PairingTool<Integer, String>(
						new Integer(this.getVariableNumber(functionName)), functionName));
				classStr += functionName;
				classStr += "(" + type + " " + this.myInitSubString(name) + "Tmp)\n";
				String shortName = this.myInitSubString(name);
				classStr += "\t{\n";
				classStr += "\t\tif(" + shortName + "Tmp > " + name + ")\n";
				classStr += "\t\t{\n";
				classStr += "\t\t\t" + name + " = " + shortName + "Tmp;\n";
				classStr += "\t\t}\n\t}\n";
			}
		}
		return classStr;
	}

	//Generate MinFunctions
	private String addJavaMinFunction(String classStr)
	{
		classStr += "\n\t//min functions.\n";
		for(int i = 0; i != this.FirstAllTypeName.size(); i++)
		{
			if(this.FirstAllTypeName.get(i).getSecond().contains("min"))
			{
				String name = this.FirstAllTypeName.get(i).getSecond();
				String type = this.FirstAllTypeName.get(i).getFirst();
				classStr += "\tpublic void ";
				String functionName = "set_" + name;
				this.FirstFunctionNumberNames.add(new PairingTool<Integer, String>(
						new Integer(this.getVariableNumber(functionName)), functionName));
				classStr += functionName;
				classStr += "(" + type + " " + this.myInitSubString(name) + "Tmp)\n";
				String shortName = this.myInitSubString(name);
				classStr += "\t{\n";
				classStr += "\t\tif(" + shortName + "Tmp < " + name + ")\n";
				classStr += "\t\t{\n";
				classStr += "\t\t\t" + name + " = " + shortName + "Tmp;\n";
				classStr += "\t\t}\n\t}\n";
			}
		}
		return classStr;
	}
	
	//Get the number of a method
	//1_avg_length => 1
	private int getFunctionNameFirstNumber(String fname)
	{
		int ireturn = 0;
		for(int i = 0; i != fname.length(); i++)
		{
			if(fname.charAt(i) == '_')
			{
				String tmp = fname.substring(0, i);
				ireturn = Integer.parseInt(tmp);
				break;
			}
		}
		if(ireturn == 0)
		{
			System.out.println("Error in getFunctionNameFirstNumber");
		}
		return ireturn;
	}
}