//Interface using SWT

import org.eclipse.swt.widgets.*; 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Interface
{
	//Database data
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static InterfaceInfo intInfo;
	static DBMeta DBMeta = new DBMeta();
	static MakeMF makeMFCode = new MakeMF();
	static Connection connection;
	static Shell shell;
	public static final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE;
	static String openFileDirectory = null;

	//Text boxes
	static Text textFileDirectory;
	static Text textSaveDirectory;
	static Text textSelectedAttributes;
	static Text textNumofGroupingVar;
	static Text textGroupingAttri;
	static Text textFVect;
	static Text textSelectCondition;
	static Text textHavingCondition;
	static Text textDBURL;
	static Text textUserName;
	static Text textPsw;
	static Text textTableName;
	
	//main funct
	public static void main(String[] args)
	{
		try
		{
			Class.forName(JDBC_DRIVER);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		//Watches for Make Code button press
		class GenerateListener implements SelectionListener
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{}
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if(intInfo == null)
				{
					intInfo = new InterfaceInfo();
				}
				try
				{
					connection = DriverManager.getConnection(intInfo.strDBURL, intInfo.strDBUserName, intInfo.strDBPSW);
					checkDBMeta();
					DBMeta.setStructTypeJAVA();
					FileToArray fileToArray = new FileToArray();
					if(textSelectedAttributes.getText().equals("") == false)
					{
						fileToArray.setSelectAttributes(textSelectedAttributes.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					if(textNumofGroupingVar.getText().equals("") == false)
					{
						fileToArray.setGroupingAttrNumber(textNumofGroupingVar.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					if(textGroupingAttri.getText().equals("") == false)
					{
						fileToArray.setGroupingAttrs(textGroupingAttri.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					if(textFVect.getText().equals("") == false)
					{
						fileToArray.setFV(textFVect.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					if(textSelectCondition.getText().equals("") == false)
					{
						fileToArray.setConditions(textSelectCondition.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					if(textHavingCondition.getText().equals("") == false)
					{
						fileToArray.setHaving(textHavingCondition.getText());
					}
					else
					{
						System.out.println("Invalid input");
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Invalid input");
						message.open();
						return;
					}
					makeMFCode = new MakeMF();
					makeMFCode.setClassString(fileToArray, DBMeta);
					MakeMain gCode = new MakeMain(textSaveDirectory.getText(),intInfo, fileToArray, makeMFCode, DBMeta);
					gCode.printGCode();
					connection.close();
					System.out.println("Codes are made. Check Destination.");
				}
				catch (SQLException e) 
				{
					System.out.println("Could not connect to database.");
					MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
					message.setText("Warning:");
					message.setMessage("Could not connect to database.");
					message.open();
				}
			}
		}
		
		//check database connection listener.
		class CheckListener implements SelectionListener
		{
			public void widgetDefaultSelected(SelectionEvent arg0)
			{}
			public void widgetSelected(SelectionEvent arg0)
			{
				intInfo = new InterfaceInfo();
				intInfo.strDBURL = textDBURL.getText();
				intInfo.strDBUserName = textUserName.getText();
				intInfo.strDBPSW = textPsw.getText();
				intInfo.strTableName = textTableName.getText();
				try
				{
					connection = DriverManager.getConnection(intInfo.strDBURL, intInfo.strDBUserName, intInfo.strDBPSW);
					DatabaseMetaData dbm = connection.getMetaData();
					// Check if sales table exists
					ResultSet tables = dbm.getTables(null, null, intInfo.strTableName, null);
					if (tables.next())
					{
						MessageBox message = new MessageBox(shell, SWT.ICON_WORKING);
						message.setMessage("Connected to Database.\nTable \"" + 
						intInfo.strTableName + "\"exists.");
						message.open();
					}
					else
					{
						MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
						message.setText("Warning:");
						message.setMessage("Connected to Database.\nTable \"" +
						intInfo.strTableName + "\" does not exist.");
						message.open();
					}
				} 
				catch (SQLException e) 
				{
					System.out.println("Cannot connect to Database.");
					MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);
					message.setText("Warning:");
					message.setMessage("Cannot connect to Database.");
					message.open();
				}
			}
		}
		
		//Watches for file input
		class OpenListener implements SelectionListener
		{
			BufferedReader br = null;
			
			public void widgetSelected(SelectionEvent event) {
				FileToArray mfStructTmp = new FileToArray();
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("../sales/test_examples");
				String[] filterExt = { "*.txt" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				if(selected != null)
				{
					System.out.println("Open File Dir:" + selected);
					openFileDirectory = selected;
					textFileDirectory.setText(selected);
				}
				try
				{
					br = new BufferedReader(new FileReader(openFileDirectory));

					String currentLine;
					while((currentLine = br.readLine()) != null)
					{
						if(currentLine.equals("SELECT ATTRIBUTE(S):"))
						{
							mfStructTmp.setSelectAttributes(br, currentLine);
							continue;
						}
						else if(currentLine.equals("NUMBER OF GROUPING VARIABLES(n):"))
						{
							currentLine = br.readLine();
							mfStructTmp.setGroupingAttrNumber(currentLine);
							continue;
						}
						else if(currentLine.equals("GROUPING ATTRIBUTES(V):"))
						{
							mfStructTmp.setGroupingAttrs(br, currentLine);
							continue;
						}
						else if(currentLine.equals("F-VECT([F]):"))
						{
							mfStructTmp.setFV(br, currentLine);
							continue;
						}
						else if(currentLine.equals("SELECT CONDITION-VECT([]):"))
						{
							mfStructTmp.setConditions(br, currentLine);
							continue;
						}
						else if(currentLine.equals("HAVING CONDITION(G):"))
						{
							mfStructTmp.setHaving(br, currentLine);
							continue;
						}
					}
				}
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				String strSelAttr = new String();
				for(int i = 0; i != mfStructTmp.FirstSelectAttr.size(); i++)
				{
					strSelAttr += mfStructTmp.FirstSelectAttr.get(i);
					if(i != mfStructTmp.FirstSelectAttr.size() - 1)
					{
						strSelAttr += ", ";
					}
				}
				if(strSelAttr != null)
				{
					textSelectedAttributes.setText(strSelAttr);
				}
				int iNumofGroupVar = 0;
				iNumofGroupVar = mfStructTmp.numGroupingVari;
				if(iNumofGroupVar != 0)
				{
					textNumofGroupingVar.setText(new Integer(iNumofGroupVar).toString());
				}
				String strGroupingAttr = new String();
				for(int i = 0; i != mfStructTmp.FirstGroupingAttr.size(); i++)
				{
					strGroupingAttr += mfStructTmp.FirstGroupingAttr.get(i);
					if(i != mfStructTmp.FirstGroupingAttr.size() - 1)
					{
						strGroupingAttr += ", ";
					}
				}
				if(strGroupingAttr != null)
				{
					textGroupingAttri.setText(strGroupingAttr);
				}
				String strFVect = new String();
				for(int i = 0; i != mfStructTmp.FirstFV.size(); i++)
				{
					strFVect += mfStructTmp.FirstFV.get(i);
					if(i != mfStructTmp.FirstFV.size() - 1)
					{
						strFVect += ", ";
					}
				}
				if(strFVect != null)
				{
					textFVect.setText(strFVect);
				}
				String strSelectionCondition = new String();
				for(int i = 0; i != mfStructTmp.FirstConditions.size(); i++)
				{
					strSelectionCondition += mfStructTmp.FirstConditions.get(i);
					if(i != mfStructTmp.FirstConditions.size() - 1)
					{
						strSelectionCondition += ", ";
					}
				}
				if(strSelectionCondition != null)
				{
					textSelectCondition.setText(strSelectionCondition);
				}
				String strHavingCondition = new String();
				for(int i = 0; i != mfStructTmp.FirstHaving.size(); i++)
				{
					strHavingCondition += mfStructTmp.FirstHaving.get(i);
					if(i != mfStructTmp.FirstHaving.size() - 1)
					{
						strHavingCondition += ", ";
					}
				}
				if(strHavingCondition != null)
				{
					textHavingCondition.setText(strHavingCondition);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		}
		
		//Save file button selection
		class SaveListener implements SelectionListener
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dd = new DirectoryDialog(shell);
				dd.setText("Save");
				dd.setMessage("Select Directory:");
				dd.setFilterPath("./");
				String selected = dd.open();
				if(selected != null)
				{
					System.out.println("Save Files Dir:" + selected);
					textSaveDirectory.setText(selected);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		}

		Display display = new Display(); 
		
		shell = new Shell(display, SHELL_TRIM & (~SWT.RESIZE));
		
		//Make Two Tabs
		TabFolder folder = new TabFolder(shell, SWT.NULL);
		Composite compAll0 = new Composite(folder, SWT.NULL);
		compAll0.setLayout(new GridLayout(1, false));
		compAll0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		shell.setLayout(new GridLayout(1, false));

		//First Tab
		TabItem tab0 = new TabItem(folder, SWT.NULL);
		tab0.setText("Database Connection");
		Composite compAll1 = new Composite(folder, SWT.NULL);
		tab0.setControl(compAll1);
		compAll1.setLayout(new GridLayout(1, false));
		compAll1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//Second Tab
		TabItem tab1 = new TabItem(folder, SWT.NULL);
		tab1.setText("User Input");
		
		//Input of file
		Group group0 = new Group(compAll0, SWT.SHADOW_IN);
		group0.setText("Choose File:");

		group0.setLayout(new GridLayout(2, false));
		group0.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		textFileDirectory = new Text(group0, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 375;
		textFileDirectory.setLayoutData(gridData);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		Button btLoadFile = new Button(group0, SWT.PUSH);
		btLoadFile.setText("Browse");
		btLoadFile.setLayoutData(gridData);
		btLoadFile.setSize(btLoadFile.computeSize(SWT.NONE, SWT.DEFAULT));
		btLoadFile.addSelectionListener(new OpenListener());

		//Textboxes for User Input
		Group group1 = new Group(compAll0, SWT.SHADOW_IN);
		group1.setText("User Input:");
		group1.setLayout(new GridLayout(1, false));
		group1.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false));
		Label lSelectedAttributes = new Label(group1, SWT.RIGHT);
		lSelectedAttributes.setText("SELECT ATTRIBUTE(S):");
		textSelectedAttributes = new Text(group1, SWT.BORDER);
		GridData gd1 = new GridData();
		gd1.widthHint = 400;
		textSelectedAttributes.setLayoutData(gd1);
		Label lNumofGroupingVar = new Label(group1, SWT.RIGHT);
		lNumofGroupingVar.setText("NUMBER OF GROUPING VARIABLES(n):");
		textNumofGroupingVar = new Text(group1, SWT.BORDER);
		textNumofGroupingVar.setLayoutData(gd1);
		Label lGroupingAttri = new Label(group1, SWT.RIGHT);
		lGroupingAttri.setText("GROUPING ATTRIBUTES(V):");
		textGroupingAttri = new Text(group1, SWT.BORDER);
		textGroupingAttri.setLayoutData(gd1);
		Label lFVect = new Label(group1, SWT.RIGHT);
		lFVect.setText("F-VECT([F]):");
		textFVect = new Text(group1, SWT.BORDER);
		textFVect.setLayoutData(gd1);
		Label lSelectCondition = new Label(group1, SWT.RIGHT);
		lSelectCondition.setText("SELECT CONDITION-VECT([]):");
		textSelectCondition = new Text(group1, SWT.BORDER);
		textSelectCondition.setLayoutData(gd1);
		Label lHavingCondition = new Label(group1, SWT.RIGHT);
		lHavingCondition.setText("HAVING CONDITIONS(G):");
		textHavingCondition = new Text(group1, SWT.BORDER);
		textHavingCondition.setLayoutData(gd1);
		
		//Generate Code Destination
		Group group2 = new Group(compAll0, SWT.SHADOW_IN);
		group2.setText("Generated Codes Destination:");
		group2.setLayout(new GridLayout(2, false));
		group2.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		textSaveDirectory = new Text(group2, SWT.BORDER);
		textSaveDirectory.setText("./");
		GridData gd2 = new GridData();
		gd2.widthHint = 300;
		textSaveDirectory.setLayoutData(gd2);
		Button btSaveDir = new Button(group2, SWT.PUSH);
		btSaveDir.setLayoutData(gridData);
		btSaveDir.setText("Browse");
		btSaveDir.setSize(btSaveDir.computeSize(SWT.NONE, SWT.DEFAULT));
		btSaveDir.addSelectionListener(new SaveListener());
		
		Composite comp0 = new Composite(compAll0, SWT.NONE);
		comp0.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
		
		FormLayout fl = new FormLayout();
		fl.marginTop = 15;
		fl.marginRight = 100;
		comp0.setLayout(fl);
		Button btGenerateCode = new Button(comp0,SWT.CENTER);
		btGenerateCode.setText("Output Code");
		
		btGenerateCode.addSelectionListener(new GenerateListener());
		
		tab1.setControl(compAll0);
		
		
		
		//DB URL, user, psw, table...
		Composite comp10 = new Composite(compAll1, SWT.NULL);
		comp10.setLayout(new GridLayout(2, false));
		comp10.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Label lDBURL = new Label(comp10, SWT.NULL);
		lDBURL.setText("DB URL:");
		lDBURL.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		textDBURL = new Text(comp10, SWT.BORDER);
		GridData gd3 = new GridData();
		gd3.horizontalAlignment = SWT.LEFT;
        gd3.verticalAlignment = SWT.TOP;
        gd3.grabExcessHorizontalSpace = false;
        gd3.grabExcessVerticalSpace = false;
		gd3.widthHint = 300;
		textDBURL.setLayoutData(gd3);
		textDBURL.setText("jdbc:postgresql://localhost:5432/sales");
		
		Composite comp11 = new Composite(compAll1, SWT.NULL);
		comp11.setLayout(new GridLayout(2, false));
		comp11.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Label lUserName = new Label(comp11, SWT.NULL);
		lUserName.setText("DB username:");
		lUserName.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		textUserName = new Text(comp11, SWT.BORDER);
		textUserName.setText("postgres");
		textUserName.setLayoutData(gd3);
		
		Composite comp12 = new Composite(compAll1, SWT.NULL);
		comp12.setLayout(new GridLayout(2, false));
		comp12.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Label lPassword = new Label(comp12, SWT.NULL);
		lPassword.setText("Password:");
		lPassword.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		textPsw = new Text(comp12, SWT.PASSWORD | SWT.BORDER);
		textPsw.setLayoutData(gd3);
		
		Composite comp13 = new Composite(compAll1, SWT.NULL);
		comp13.setLayout(new GridLayout(2, false));
		comp13.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Label lTableName = new Label(comp13, SWT.NULL);
		lTableName.setText("Table name:");
		lTableName.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		textTableName = new Text(comp13, SWT.BORDER);
		textTableName.setText("sales");
		textTableName.setLayoutData(gd3);
		
		Composite comp14 = new Composite(compAll1, SWT.NULL);
		comp14.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, true));
		comp14.setLayout(fl);
		
		Button btCheckDB = new Button(comp14, SWT.CENTER);
		btCheckDB.setText("Test Connection");
		btCheckDB.addSelectionListener(new CheckListener());
		shell.pack(); 
		btLoadFile.pack();
		shell.setSize(500, 540);
		shell.setLocation(100, 100);
		shell.open(); 
		while(!shell.isDisposed()) 
			if(!display.readAndDispatch()) 
				display.sleep(); 
		display.dispose(); 
		btLoadFile.dispose();
	}
	
	//Check metadata for database
	private static void checkDBMeta()
	{
		if(intInfo == null)
		{
			intInfo = new InterfaceInfo();
		}
		String SQLQuery = "select column_name, data_type from information_schema.columns\n"
				+ "where table_name = '" + intInfo.strTableName + "'";
		DBMeta = new DBMeta();
		try
		{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(SQLQuery);
			while(rs.next())
			{
				String col = rs.getString("column_name");
				String type = rs.getString("data_type");
				DBMeta.addValue(col, type);
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	//Print metadata and check if correct
	@SuppressWarnings("unused")
	private static void printDBMeta(DBMeta DBMeta)
	{
		for(int i = 0; i != DBMeta.getList().size(); i++)
		{
			System.out.println( DBMeta.getList().get(i).getFirst() + "\t"
					+ DBMeta.getList().get(i).getSecond());
		}
	}
}
