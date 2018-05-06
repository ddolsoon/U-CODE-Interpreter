import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Dialog;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.io.*;
import java.nio.channels.SelectableChannel;
import java.text.DecimalFormat;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class UCodeInterpreterFrame {

	static JFrame frmUcode;
	UCodeInterpreter interpreter= new UCodeInterpreter();
	static JTextArea ResultTextArea;
	static JTextArea InstrSeqTextArea;
	static JTextArea StackTextArea;
	static String FileName;
	static boolean StepExecuteSelected;
	static boolean LabelExecuteSelected;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UCodeInterpreterFrame window = new UCodeInterpreterFrame();
					window.frmUcode.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UCodeInterpreterFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frmUcode = new JFrame();
		frmUcode.setTitle("U-CODE \uC778\uD130\uD504\uB9AC\uD130");
		frmUcode.getContentPane().setBackground(SystemColor.info);
		frmUcode.setBounds(100, 100, 984, 752);
		frmUcode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUcode.getContentPane().setLayout(null);
		
		JLabel StackLabel = new JLabel("Stack");
		StackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		StackLabel.setFont(new Font("���� ���", Font.BOLD, 20));
		StackLabel.setBounds(646, 10, 75, 26);
		frmUcode.getContentPane().add(StackLabel);
		
		JLabel LabelTableLabel = new JLabel("\uB808\uC774\uBE14 \uD14C\uC774\uBE14");
		LabelTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		LabelTableLabel.setFont(new Font("���� ���", Font.BOLD, 19));
		LabelTableLabel.setBounds(733, 8, 223, 33);
		frmUcode.getContentPane().add(LabelTableLabel);
		
		StepExecuteSelected = false;
		LabelExecuteSelected = false;

		//���� ��ư
		JButton ExecuteButton = new JButton("\uC2E4\uD589");
		ExecuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE�� �����Ͻðڽ��ϱ�?","U-CODE ����",JOptionPane.YES_NO_OPTION);
				//�ܰ躰�� ���� �߿�, ������ ������찡 �����Ƿ�, �κ������� �ʱ�ȭ ���ش�.
				InstrSeqTextArea.setText("");
				InstrSeqTextArea.append("Line\tInstruction\tOperand1\tOperand2\tOperand3\n");
				InstrSeqTextArea.append("-----------------------------------------------------"
						+ "---------------------------------\n");
				ResultTextArea.setText("");
				StackTextArea.setText("");
				for(int i=0;i<interpreter.InstructionCount.length;i++)
					interpreter.InstructionCount[i] = 0;
				interpreter.MemoryAccessCount = 0;
				interpreter.StatisticResult = "";
				interpreter.InstructionSequence.removeAllElements();
				if(FileName == null)
				{
					JOptionPane.showMessageDialog(frmUcode, "������ ���������ʽ��ϴ�!",
							"���� ����",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("�ڵ带 �о���̴���............................\n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("���� ��ɾ�\n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				int i=0;
				while(interpreter.InstructionBuffer[i] != null)
				{
						ResultTextArea.append(interpreter.InstructionBuffer[i] + "\n");						
						i++;
				}
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("Assembling.....................................\n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("Executing......................................\n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("���� ��� \n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				
				if(result == JOptionPane.YES_OPTION)
				{
					interpreter.execute();
				}
				else
				{
					return;
				}
				ResultTextArea.append("\n-------------------------------------------------------------------------------------------------\n");
			}
		});
		ExecuteButton.setFont(new Font("���� ���", Font.BOLD, 15));
		ExecuteButton.setBounds(745, 271, 211, 33);
		frmUcode.getContentPane().add(ExecuteButton);
		
		//�ܰ躰�� ���� ��ư
		JButton StepButton = new JButton("\uB2E8\uACC4\uBCC4\uB85C \uC2E4\uD589");
		StepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StepExecuteSelected == false)
				{
					int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE�� �ܰ躰�� �����Ͻðڽ��ϱ�?","U-CODE ����",JOptionPane.YES_NO_OPTION);
				
					if(FileName == null)
					{
						JOptionPane.showMessageDialog(frmUcode, "������ ���������ʽ��ϴ�!",
								"���� ����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(result == JOptionPane.YES_OPTION)
					{
						interpreter.StepExecute();
					}
				}
				else
					interpreter.StepExecute();
			}
		});
		StepButton.setFont(new Font("���� ���", Font.BOLD, 16));
		StepButton.setBounds(745, 313, 211, 33);
		frmUcode.getContentPane().add(StepButton);
		
		//������� ���� ��ư
		JButton StatisticButton = new JButton("\uD1B5\uACC4\uD30C\uC77C \uC0DD\uC131");
		StatisticButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(frmUcode,"�������(*.lst)�� �����Ͻðڽ��ϱ�?",
						"list���� ����",JOptionPane.YES_NO_OPTION);
				if(FileName == null)
				{
					JOptionPane.showMessageDialog(frmUcode, "������ ���������ʽ��ϴ�!",
							"���� ����",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(result == JOptionPane.YES_OPTION)
				{
					interpreter.statistic(FileName);
					JOptionPane.showMessageDialog(frmUcode, "�������(*.lst�� �����Ǿ����ϴ�.",
							"������� ����",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					return;
				}
			}
		});
		StatisticButton.setFont(new Font("���� ���", Font.BOLD, 16));
		StatisticButton.setBounds(745, 399, 211, 33);
		frmUcode.getContentPane().add(StatisticButton);
		
		JLabel InstructionSquenceLabel = new JLabel("\uBA85\uB839\uC5B4 \uC2E4\uD589 \uACFC\uC815");
		InstructionSquenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		InstructionSquenceLabel.setFont(new Font("���� ���", Font.BOLD, 24));
		InstructionSquenceLabel.setBounds(0, 6, 621, 33);
		frmUcode.getContentPane().add(InstructionSquenceLabel);
		
		JLabel ResultLabel = new JLabel("\uC2E4\uD589 \uACB0\uACFC");
		ResultLabel.setFont(new Font("���� ���", Font.BOLD, 20));
		ResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ResultLabel.setBounds(20, 421, 712, 33);
		frmUcode.getContentPane().add(ResultLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 46, 611, 372);
		frmUcode.getContentPane().add(scrollPane);
		
		//��ɾ� ���� â
		InstrSeqTextArea = new JTextArea();
		InstrSeqTextArea.setEditable(false);
		InstrSeqTextArea.setFont(new Font("���� ���", Font.BOLD, 16));
		scrollPane.setViewportView(InstrSeqTextArea);
		InstrSeqTextArea.append("Line\tInstruction\tOperand1\tOperand2\tOperand3\n");
		InstrSeqTextArea.append("-----------------------------------------------------"
				+ "---------------------------------\n");

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(646, 46, 75, 372);
		frmUcode.getContentPane().add(scrollPane_1);
		
		//���� ���� â
		StackTextArea = new JTextArea();
		StackTextArea.setEditable(false);
		StackTextArea.setFont(new Font("���� ���", Font.BOLD, 16));
		scrollPane_1.setViewportView(StackTextArea);
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 457, 720, 247);
		frmUcode.getContentPane().add(scrollPane_2);
		
		//���� ��� â
		ResultTextArea = new JTextArea();
		scrollPane_2.setViewportView(ResultTextArea);
		ResultTextArea.setFont(new Font("���� ���", Font.BOLD, 16));
		ResultTextArea.setForeground(new Color(0, 0, 0));
		ResultTextArea.setEditable(false);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(733, 53, 223, 208);
		frmUcode.getContentPane().add(scrollPane_3);
		
		//���̺� ���̺� â
		JTextArea LableTableTextArea = new JTextArea();
		scrollPane_3.setViewportView(LableTableTextArea);
		LableTableTextArea.setFont(new Font("���� ���", Font.BOLD, 16));
		LableTableTextArea.setForeground(Color.BLACK);
		LableTableTextArea.append("Address\tLable");
		LableTableTextArea.append("\n-------------------------------\n");
		
		LableTableTextArea.setEditable(false);
		
		//���� �ҷ����� ��ư
		JButton FileLoadButton = new JButton("\uD30C\uC77C \uBD88\uB7EC\uC624\uAE30");
		FileLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser FileOpenManager = new JFileChooser("C:\\Users\\Administrator\\Desktop");
				FileOpenManager.setFileFilter(new FileNameExtensionFilter("*.uco", "uco"));
				//���� �ҷ��� ���, ��� ���� ���� ���� �ʱ�ȭ------------------------------------
				interpreter.LabelTable.removeAllElements();
				for(int i=0;i<interpreter.InstructionBuffer.length;i++)
					interpreter.InstructionBuffer[i] = null;
				interpreter.InstructionSequence.removeAllElements();
				interpreter.CodeLine.removeAllElements();
				for(int i=0;i<interpreter.BlockAddress.length;i++)
					interpreter.BlockAddress[i]	= 0;
				interpreter.AllInstructionCount = 0;
				interpreter.AllInstructionCycle = 0;
				for(int i=0;i<interpreter.InstructionCount.length;i++)
					interpreter.InstructionCount[i] = 0;
				interpreter.StatisticResult = "";
				interpreter.PC = 0;
				interpreter.StepLine = 1;
				StepExecuteSelected = false;
				LabelExecuteSelected = false;
				ResultTextArea.setText("");
				InstrSeqTextArea.setText("");
				StackTextArea.setText("");
				LableTableTextArea.setText("");
				LableTableTextArea.append("Address\tLable");
				LableTableTextArea.append("\n-------------------------------\n");
				InstrSeqTextArea.append("Line\tInstruction\tOperand1\tOperand2\tOperand3\n");
				InstrSeqTextArea.append("-----------------------------------------------------"
						+ "---------------------------------\n");
				//-------------------------------------------------------------------------------
				
	      		FileOpenManager.showDialog(frmUcode,"U-CODE �ҷ�����");
	      		FileName = FileOpenManager.getSelectedFile().getPath();
	      		if(FileName == null)
	      			return;
	      		
	      		System.out.println(FileName);
	      		interpreter.readUcode(FileName);
	      		interpreter.assemble();
	      		Iterator<Label> it = interpreter.LabelTable.iterator();
	      		while(it.hasNext())
	      		{
	      			Label label = it.next();
	      			LableTableTextArea.append(Integer.toString(label.address)+"\t");
	      			LableTableTextArea.append(label.label);
	      			LableTableTextArea.append("\n");
	      		}
	      		StepExecuteSelected = true;
	      		LabelExecuteSelected = true;
			}
		});
		
		FileLoadButton.setFont(new Font("���� ���", Font.BOLD, 16));
		FileLoadButton.setBounds(745, 442, 211, 33);
		frmUcode.getContentPane().add(FileLoadButton);
		
		JLabel MakerLabel = new JLabel("\uC81C\uC791\uC790 : \uAE40\uBBFC\uC131");
		MakerLabel.setFont(new Font("���� ���", Font.BOLD, 20));
		MakerLabel.setBounds(763, 490, 178, 33);
		frmUcode.getContentPane().add(MakerLabel);
		
		ImageIcon imageIcon = new ImageIcon("images/watermark.png");
		JLabel WaterMarkLabel = new JLabel(imageIcon);
		WaterMarkLabel.setBounds(745, 533, 211, 157);
		frmUcode.getContentPane().add(WaterMarkLabel);
		
		//���̺� ������ ���� ��ư
		JButton LabelExecuteButton = new JButton("\uB808\uC774\uBE14 \uB2E8\uC704\uB85C \uC2E4\uD589");
		LabelExecuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(StepExecuteSelected == false)
				{
					int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE�� ���̺� ������ �����Ͻðڽ��ϱ�?","U-CODE ����",JOptionPane.YES_NO_OPTION);
				
					if(FileName == null)
					{
						JOptionPane.showMessageDialog(frmUcode, "������ ���������ʽ��ϴ�!",
								"���� ����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(result == JOptionPane.YES_OPTION)
					{
						interpreter.LableExecute();	
					}
				}
				else
					interpreter.LableExecute();	
				
			}
		});
		LabelExecuteButton.setFont(new Font("���� ���", Font.BOLD, 16));
		LabelExecuteButton.setBounds(745, 356, 211, 33);
		frmUcode.getContentPane().add(LabelExecuteButton);
	}
}

/*
	��ɾ� ������
	nop,bgn,sym,						//���α׷� ���� ���
	lod,lda,ldc,str,ldi,sti,			// ������ �̵� ������
	not,neg,inc,dec,dup,				//���� ������
	add,sub,mult,div,mod,				//���� ������(���)
	gt,lt,ge,le,eq,ne,and,or,			//���� ������(��)
	swp,								//���� ������(Swap)
	ujp,tjp,fjp,						//�帧 ����
	call,ret,retv,ldp,proc,end			//�Լ� ���� �� ȣ��
	
}//�� ��ɾ� �� : 37�� ���
*/

//Label Ŭ����
class Label{
	String label;
	int address;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	
}

class UCodeInterpreter {
	 int [] Memory;			//�޸�
	 int PC;				//���α׷� ī����
	 int [] BlockAddress;	//�޸� ��� �����ּ�
	 Stack stack;			//����(�ӽ����� ��� ��ġ)

	 Vector<String> CodeLine = new Vector<String>();
	 Vector<Label> LabelTable = new Vector<Label>();
	 String [] InstructionBuffer;
	 
	 Stack ReturnStack;	//�����ּ� ���� ����
	 int ProgrameEnd;	//���α׷��� ��
	 
	 
	 
	 //��迡 ����� ����
	 String StatisticResult;	//������
	 Vector<String> InstructionSequence = new Vector<String>();	//��ɾ� �������
	//��ɾ� �� �̸�
	 String [] InstructionName = {
				"nop","ldi","sti","not","neg",
				"inc","dec","dup","add","sub",
				"mult","div","mod","gt","lt",
				"ge","le","eq","ne","and","or",
				"swp","ldp","ret","retv","end",
				"bgn","ujp","tjp","fjp","ldc",
				"call","lod","lda","str","proc",
				"sym"
		};
	 
	 //��ɾ� �� ����Ŭ(������ �̸��� ����)
	 int [] InstructionCycle = {
			 5,10,10,5,5,
			 1,1,5,10,10,
			 50,100,100,20,20,
			 20,20,20,20,10,10,
			 10,10,30,30,0,
			 0,10,10,10,5,
			 30,5,5,5,30,
			 0
	 };
			 
			 
	 
	 int [] InstructionCount;	//��ɾ� �� ����Ƚ��
	 int AllInstructionCount;	//����� ��ɾ� �� Ƚ��
	 int MemoryAccessCount;		//�޸� ����Ƚ�� lod,lda,str,sti,ldi
	 long AllInstructionCycle; //�����ϴµ� ���� �� ����Ŭ ��
	 
	 int StepLine;
	 
	public UCodeInterpreter() {
		// TODO Auto-generated constructor stub
		Memory = new int[2000];
		BlockAddress = new int[200];
		stack = new Stack();
		ReturnStack = new Stack();
		StepLine = 1;
		//��� ó�� ����ּҴ� 0���� �ʱ�ȭ.
		for(int i=0;i<BlockAddress.length; i++)
			BlockAddress[i] = 0;
		//���� ����� ����� ���ϸ�,���� ����� �����ּ�
		
		
		InstructionBuffer = new String[2000];
		
		PC = 0;
		InstructionCount = new int[37];
		/*
		 * ��ɾ� ����
		 * nop  ldi  sti  not  neg
		 * inc  dec  dup  add  sub
		 * mult div  mod  gt   lt
		 * ge   le   eq   ne   and
		 * or   swp  ldp  ret  retv
		 * end  bgn  ujp  tjp  fjp
		 * ldc  call lod  lda  str
		 * proc sym
		 */
		for(int i=0; i<InstructionCount.length;i++)
			InstructionCount[i] = 0; //��� �ʱ� ����Ƚ�� 0���� �ʱ�ȭ
		
		StatisticResult = "";
		MemoryAccessCount = 0;
		AllInstructionCount = 0;
		AllInstructionCycle = 0;
		
	}//End UCodeInterpreter()
	
	void readUcode(String FileName)
	{
		
		//----------------��ü�ڵ� �о����-----------------------
		try {
			
			Scanner fileScanner = new Scanner(new FileReader(FileName));
					
			while(fileScanner.hasNext()){
				CodeLine.add(fileScanner.nextLine());	//�� ���ξ� �о ����
			}
			
			fileScanner.close();
		} catch (FileNotFoundException  e) {
			// TODO: handle exception
			System.out.println("���� ���� ���� ����");
		}
		//---------------------------------------------------------
		
	}//End readUcode(String FileName)
	
	void assemble(){
		
		System.out.println("���̺� ���̺�");
		System.out.println("-------------------------------------------------------");
		//���̺� ����---------------------------------------------------
		Iterator<String> codeit = CodeLine.iterator();//Iterator ��ü ���(���̺� ����)
		int Line = 0;
		while(codeit.hasNext()){
			
			//���̺� �����ϱ�
			String Label = codeit.next();
			Label = Label.substring(0,10);	//���̺� ����
			Label = Label.trim();	//���� ����
			if(!Label.equals(""))//������ �ƴ϶��, ���̺� ���̺� ����
			{
				Label label = new Label();
				label.label = Label;
				label.address = Line;

				LabelTable.add(label);
				System.out.println("label:" + label.label + "\t\taddress : " + label.address);
			}	
			Line++;
		}
		//--------------------------------------------------------------------
		
		System.out.println("-------------------------------------------------------");
		System.out.println("���� ��ɾ�");
		System.out.println("-------------------------------------------------------");
		//��ɾ� �����ϱ�---------------------------------------------------------------
		Iterator<String> Insit = CodeLine.iterator();//Iterator ��ü ���(��ɾ� ����)
		char comment = 0;
		int j = 0;
		while(Insit.hasNext()){
			String Instruction = Insit.next();
			
			int i;
			i=0;
			comment = 0;
			for(;i<Instruction.length();i++)
			{	
				//�ּ��κ��� �ǳʶ�
				if(Instruction.charAt(i) == '%')
				{
					comment = 1;
					break;
				}
			}
			if(comment == 1)	//�ּ����ִٸ�, �ּ� �������� ��ɾ� ����
				Instruction = Instruction.substring(11,i-1);
			else				//�ּ��̾��ٸ�, ��ü ��ɾ� ����
				Instruction = Instruction.substring(11);
			
			InstructionBuffer[j++] = Instruction;
			
		}
		//----------------------------------------------------------------------------
		
		
		
		//���� ��ɾ� ���
		for(int i=0; i < j ;i++)
			System.out.println(i + " " + InstructionBuffer[i]);
		
		//�޸� ��� �Ҵ�(bgn,proc)---------------------------------------------------------------
		for(int i=0; i< j ; i++)
		{
			String Chunk = InstructionBuffer[i];
			StringTokenizer instruction = new StringTokenizer(Chunk, " ");
			String memoryInst = instruction.nextToken();
			
			switch(memoryInst)
			{
			case "bgn":
				int bgnvalue1 =Integer.parseInt(instruction.nextToken());
				BlockAddress[1] = bgnvalue1;
				break;
			case "proc":
				//���� ����� �����ּҸ� ����
				int procvalue1 = Integer.parseInt(instruction.nextToken());	//��� ��ȣ
				int procvalue2 = Integer.parseInt(instruction.nextToken());	//��� ������
				//���� ����� �����ּҸ� �ҷ��´�.
				BlockAddress[procvalue1 + 1] = BlockAddress[procvalue1];
				//����� �����ŭ ���Ѵ�.
				BlockAddress[procvalue1 + 1] += procvalue2;
				break;
			
			}
		}	
		//-----------------------------------------------------------------------------------------
			
	}//End assemble()
	
	void execute()
	{	
		PC = 0;
		int temp;
		int temp2;
		int Line = 1;
		//��ɾ����
		while(PC >= 0)
		{
			String Chunk = InstructionBuffer[PC];
			StringTokenizer instruction = new StringTokenizer(Chunk, " ");
			
			switch(instruction.nextToken())
			{
			//0-address ��ɾ�
			case "nop":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				//�ܼ� ���̺��� ��ġ�� ��Ÿ���Ƿ� �ƹ� ��ɵ� ��������.
				InstructionCount[0]++;
				break;
			case "ldi":
				//���� ������� ���� pop�Ͽ� �ּҰ����� ����ϰ�, �����͸� ���ÿ� ����
				temp = stack.pop();
				
				stack.push(Memory[temp]);
				//pause();
				InstructionCount[1]++;
				break;
				
			case "sti":
				//���� �ּҹ��� �̿��� ���� ������� ���� �޸𸮿� �����Ѵ�.
				//������ ������ �ּҿ� ������ ��,�ΰ��� ���ÿ��� pop �ȴ�.
				temp = stack.pop();		//������ ��
				temp2 = stack.pop();	//������ �ּ�
				Memory[temp2] = temp;

				InstructionCount[2]++;
				break;
				
			case "not":
				temp = stack.pop();
				if(temp == 1)
					stack.push(0);
				else
					stack.push(1);
				
				InstructionCount[3]++;
				break;
				
			case "neg":
				temp = stack.pop();
				stack.push(-temp);
				InstructionCount[4]++;
				break;

			case "inc":
				temp = stack.pop();
				stack.push(++temp);
				InstructionCount[5]++;
				break;
				
			case "dec":
				temp = stack.pop();
				stack.push(--temp);
				InstructionCount[6]++;
				break;
				
			case "dup":
				temp = stack.pop();
				stack.push(temp);
				stack.push(temp);
				InstructionCount[7]++;
				break;
				
			case "add":
				temp = stack.pop();
				stack.push(stack.pop() + temp);
				InstructionCount[8]++;
				break;
				
			case "sub":
				temp = stack.pop();
				stack.push(stack.pop() - temp);
				InstructionCount[9]++;
				break;
			case "mult":
				temp = stack.pop();
				stack.push(stack.pop() * temp);
				InstructionCount[10]++;
				break;

			case "div":
				temp = stack.pop();
				stack.push(stack.pop() / temp);
				InstructionCount[11]++;
				break;
				
			case "mod":
				temp = stack.pop();
				stack.push(stack.pop() % temp);
				InstructionCount[12]++;
				break;
				
			case "gt":
				temp = stack.pop();
				temp2 = stack.pop();
				if(temp2 > temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[13]++;
				break;
				
			case "lt":
				temp = stack.pop();
				if(stack.pop() < temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[14]++;
				break;
				
				
			case"ge":
				temp = stack.pop();
				temp2 = stack.pop();
				if(temp2 >= temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[15]++;
				break;
				
			case "le":
				temp = stack.pop();
				if(stack.pop() <= temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[16]++;
				break;
				
			case "eq":
				temp = stack.pop();
				if(stack.pop() == temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[17]++;
				break;
				
			case "ne":
				temp = stack.pop();
				if(stack.pop() != temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[18]++;
				break;
				
			case "and":
				temp = stack.pop();
				temp = stack.pop() & temp;
				if(temp == 1)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[19]++;
				break;
				
			case "or":
				temp = stack.pop();
				temp = stack.pop() | temp;
				if(temp == 1)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[20]++;
				break;
				
			case "swp":
				temp = stack.pop();
				temp2 = stack.pop();
				stack.push(temp);
				stack.push(temp2);
				InstructionCount[21]++;
				break;
				
			case "ldp":
				//load prarameters��� �ǹ̷� �Լ��� �����ڵ��� ���ÿ� �����ϴ°��� ��Ÿ���µ�,
				//�� ��ɾ��� ����� ���ǵ� U-CODE�� �̹� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[22]++;
				break;
			case "ret":
				//��ȯ���� ���� ��ɾ�
				//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[23]++;
				break;
				
			case "retv":
				//��ȯ���� �ִ� ��ɾ�
				//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[24]++;
				break;
				
			case "end":
				PC = ReturnStack.pop() - 1;
				if( PC + 1== ProgrameEnd )//main ���ν��� ���������� end��ɾ���, ���α׷� ���Ḧ ��Ÿ��.
				{
					PC = -2;
					System.out.println("���α׷��� ���������� ���� �Ǿ����ϴ�. End Main");
				}
				InstructionCount[25]++;
				break;
			//1-address ��ɾ�
			case "bgn":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				InstructionCount[26]++;
				break;
				
			case "ujp":
				String ujpValue1 = instruction.nextToken();
				Iterator<Label> ujpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
				while(ujpLabelTableit.hasNext()){
					Label tableLabel = ujpLabelTableit.next();
					if(ujpValue1.equals(tableLabel.getLabel()))
					{
						PC = tableLabel.getAddress();
						
						break;
					}
					
				}
				InstructionCount[27]++;
				break;
				
			case "tjp":
				String tjpValue1 = instruction.nextToken();
				if(stack.pop() == 1)
				{
					Iterator<Label> tjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(tjpLabelTableit.hasNext()){
						Label tableLabel = tjpLabelTableit.next();
						if(tjpValue1.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress();
							break;
						}
						
					}
				}
				InstructionCount[28]++;
				break;
			case "fjp":
				String fjpValue1 = instruction.nextToken();
				if(stack.pop() == 0)
				{
					Iterator<Label> fjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(fjpLabelTableit.hasNext()){
						Label tableLabel = fjpLabelTableit.next();
						if(fjpValue1.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress();
							break;
						}
						
					}
				}
				InstructionCount[29]++;
				break;
				
			case "ldc":
				int ldcValue1 = Integer.parseInt(instruction.nextToken());
				stack.push(ldcValue1);	//������� �ִ´�.
				InstructionCount[30]++;
				break;
				
			case "call":
				String Label = instruction.nextToken();
				switch(Label)
				{
				case "write":
					int result = stack.pop();
					UCodeInterpreterFrame.ResultTextArea.append(result + "  ");
					StatisticResult = StatisticResult.concat(Integer.toString(result) + " ");
					break;
					
				case "read":
					do
					{
						String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"����� �Է�");
						if(Input == null || Input.equals(""))
						{
							continue;
						}
						else
						{
							int Result = Integer.parseInt(Input);
							UCodeInterpreterFrame.ResultTextArea.append("����� �Է� : " + Result + "\n");
							Memory[stack.pop()] = Result;
							break;
						}
						
					}while(true);
					
					break;
					
				case "lf":
					System.out.println();
					StatisticResult = StatisticResult.concat("\r\n");
					break;
				case "main":	
					ProgrameEnd = PC+1; //���α׷��� ������Ÿ���� �ּ�
					//-----------------------------------------------------------------------------------------
				default:
					//�������ǵ� �Լ��̸�,�Լ������� �� ���ƿ� �����ּ� ����
					if(Label.substring(0,2) != "$$")
						ReturnStack.push(PC + 1);
					Iterator<Label> LabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(LabelTableit.hasNext()){
						Label tableLabel = LabelTableit.next();
						if(Label.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress() - 1;
							break;
						}
						
					}
				}//End switch
				InstructionCount[31]++;
				break;
				
			//2-address ��ɾ�
			case "lod":
				int lodValue1 = Integer.parseInt(instruction.nextToken());
				int lodValue2 = Integer.parseInt(instruction.nextToken());
				stack.push(Memory[BlockAddress[lodValue1] + lodValue2]);
				InstructionCount[32]++;
				break;
				
			case "lda":
				int ldaValue1 = Integer.parseInt(instruction.nextToken());
				int ldaValue2 = Integer.parseInt(instruction.nextToken());
				stack.push(BlockAddress[ldaValue1] + ldaValue2);
				InstructionCount[33]++;
				break;
				
			case "str":
				int strValue1 = Integer.parseInt(instruction.nextToken());
				int strValue2 = Integer.parseInt(instruction.nextToken());
				Memory[BlockAddress[strValue1] + strValue2] = stack.pop();
				InstructionCount[34]++;
				break;
				
			case "proc":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				InstructionCount[35]++;
				break;
			
			//3-address ��ɾ�
			case "sym":	
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������(proc ��ɾ� ��ɿ� ���Ե�)
				InstructionCount[36]++;
				break;
				
			default:
				System.out.println("������� �ʴ� ��ɾ��Դϴ�!!!!");
				System.out.println("���α׷��� �����մϴ�...");
				System.exit(0);
				break;
			}//End switch
			
			//��ɾ� ������� ����
			if(PC >= 0)
			{
				InstructionSequence.add(CodeLine.get(PC));
				StringTokenizer st = new StringTokenizer(InstructionBuffer[PC]," ");
				UCodeInterpreterFrame.InstrSeqTextArea.append(Line+"\t");
				while(st.hasMoreTokens())
				{
					String token = st.nextToken();
					UCodeInterpreterFrame.InstrSeqTextArea.append(token+"\t");
				}
				
				UCodeInterpreterFrame.InstrSeqTextArea.append("\n");
				Line++;
			}	
			else
			{
				UCodeInterpreterFrame.InstrSeqTextArea.append(Line+"\t");
				InstructionSequence.add("           end");
				UCodeInterpreterFrame.InstrSeqTextArea.append("end \n");
				Line++;
			}

			//System.out.println(CodeLine.get(PC));
			PC++; //���� ��ɾ� ����
		}//End While

	}//End execute()
	
	void statistic(String FileName)
	{
		//������� ��� �޼ҵ�
		int index = FileName.lastIndexOf(".uco");
		FileName = FileName.substring(0,index);
		String StatisticName = FileName.concat(".lst");
		
		try {
			PrintWriter filewriter = new PrintWriter (StatisticName);
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("**********************************���� UCODE**********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("Line\tLabel       UCODE \r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			Iterator<String> codeit = CodeLine.iterator();//Iterator
			int Line = 1;
			while(codeit.hasNext()){
				filewriter.printf("%d\t%s \r\n",Line,codeit.next());
				Line++;
			}
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("********************************��ɾ� �������********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("Line\tLabel       UCODE \r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			Iterator<String> instseqit = InstructionSequence.iterator();//Iterator
			Line = 1;
			while(instseqit.hasNext()){
				filewriter.printf("%d\t%s \r\n",Line,instseqit.next());
				Line++;
			}
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("********************************���� ���********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("%s \r\n", StatisticResult);
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("***********************************��ɾ� �� ���� Ƚ��************************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");			
			for(int i=0;i<37;i++)
			{
				filewriter.printf("%s\t%d\t",InstructionName[i],InstructionCount[i]);
				if((i+1)%5 == 0)
					filewriter.printf("\r\n");
				
			}
			filewriter.printf("\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("********************************�޸� ���� Ƚ��********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");			
			MemoryAccessCount += InstructionCount[1];
			MemoryAccessCount += InstructionCount [2];
			MemoryAccessCount += InstructionCount [32];
			MemoryAccessCount += InstructionCount [33];	
			MemoryAccessCount += InstructionCount [34];
			filewriter.printf("�޸� ���� ��ɾ� : ldi,sti,lod,lda,str \r\n");
			filewriter.printf("�޸� ���� Ƚ�� : %d \r\n",MemoryAccessCount);
			filewriter.printf("---------------------------------------------------------------------------------\r\n");			
			filewriter.printf("********************************�� ��ɾ� ���� Ƚ��********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
				AllInstructionCount += InstructionCount[i];
			filewriter.printf("�� ��ɾ� ���� Ƚ�� : %d \r\n",AllInstructionCount);
			
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("***********************************��ɾ� �� ��� ����************************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
			{
				double rate = (double)((double)InstructionCount[i]/(double)AllInstructionCount)*100;
				String DigitFormat = "0.####";
				DecimalFormat form = new DecimalFormat(DigitFormat);
				String Executionrate = form.format(rate);
				Executionrate = Executionrate.concat("%");
				filewriter.printf("%s\t%s\t",InstructionName[i],Executionrate);
				if((i+1)%1 == 0)
					filewriter.printf("\r\n");
				
			}
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************��ɾ� �� �ҿ�� ����Ŭ ��********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
			{
				filewriter.printf("%s\t%d Cycle\t",InstructionName[i],InstructionCycle[i]*InstructionCount[i]);
				if((i+1)%1 == 0)
					filewriter.printf("\r\n");
			}
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************��ɾ ���� �� ����Ŭ ��********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
				AllInstructionCycle += InstructionCycle[i]*InstructionCount[i];
			filewriter.printf("���α׷��� �����ϴµ� ���� �� ����Ŭ �� : %d Cycle\r\n",AllInstructionCycle);
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************��                        ��********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************��ɾ� �� �ʿ��� ����Ŭ ��********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
			{
				filewriter.printf("%s\t%d\t",InstructionName[i],InstructionCycle[i]);
				if((i+1)%5 == 0)
					filewriter.printf("\r\n");
				
			}
			filewriter.printf("\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			
			filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("���� ����� ���� �߻�!");
			pause();
			System.exit(1);
		}
		
	}
		
	//�ܼ�â���� �ٷ� ����Ǵ°��� �����ϴ� �Լ�
	public static void pause() {
		try {
		      System.in.read();
		} catch (IOException e) { }
		  

	}//End pause()
	
	//�ܰ躰�� �����ϴ� �Լ�
	void StepExecute()
	{
		int temp;
		int temp2;
		//��ɾ����
			String Chunk = InstructionBuffer[PC];
			StringTokenizer instruction = new StringTokenizer(Chunk, " ");
			
			switch(instruction.nextToken())
			{
			//0-address ��ɾ�
			case "nop":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				//�ܼ� ���̺��� ��ġ�� ��Ÿ���Ƿ� �ƹ� ��ɵ� ��������.
				InstructionCount[0]++;
				break;
			case "ldi":
				//���� ������� ���� pop�Ͽ� �ּҰ����� ����ϰ�, �����͸� ���ÿ� ����
				temp = stack.pop();
				
				stack.push(Memory[temp]);
				//pause();
				InstructionCount[1]++;
				break;
				
			case "sti":
				//���� �ּҹ��� �̿��� ���� ������� ���� �޸𸮿� �����Ѵ�.
				//������ ������ �ּҿ� ������ ��,�ΰ��� ���ÿ��� pop �ȴ�.
				temp = stack.pop();		//������ ��
				temp2 = stack.pop();	//������ �ּ�
				Memory[temp2] = temp;

				InstructionCount[2]++;
				break;
				
			case "not":
				temp = stack.pop();
				if(temp == 1)
					stack.push(0);
				else
					stack.push(1);
				
				InstructionCount[3]++;
				break;
				
			case "neg":
				temp = stack.pop();
				stack.push(-temp);
				InstructionCount[4]++;
				break;

			case "inc":
				temp = stack.pop();
				stack.push(++temp);
				InstructionCount[5]++;
				break;
				
			case "dec":
				temp = stack.pop();
				stack.push(--temp);
				InstructionCount[6]++;
				break;
				
			case "dup":
				temp = stack.pop();
				stack.push(temp);
				stack.push(temp);
				InstructionCount[7]++;
				break;
				
			case "add":
				temp = stack.pop();
				stack.push(stack.pop() + temp);
				InstructionCount[8]++;
				break;
				
			case "sub":
				temp = stack.pop();
				stack.push(stack.pop() - temp);
				InstructionCount[9]++;
				break;
			case "mult":
				temp = stack.pop();
				stack.push(stack.pop() * temp);
				InstructionCount[10]++;
				break;

			case "div":
				temp = stack.pop();
				stack.push(stack.pop() / temp);
				InstructionCount[11]++;
				break;
				
			case "mod":
				temp = stack.pop();
				stack.push(stack.pop() % temp);
				InstructionCount[12]++;
				break;
				
			case "gt":
				temp = stack.pop();
				temp2 = stack.pop();
				if(temp2 > temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[13]++;
				break;
				
			case "lt":
				temp = stack.pop();
				if(stack.pop() < temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[14]++;
				break;
				
				
			case"ge":
				temp = stack.pop();
				temp2 = stack.pop();
				if(temp2 >= temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[15]++;
				break;
				
			case "le":
				temp = stack.pop();
				if(stack.pop() <= temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[16]++;
				break;
				
			case "eq":
				temp = stack.pop();
				if(stack.pop() == temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[17]++;
				break;
				
			case "ne":
				temp = stack.pop();
				if(stack.pop() != temp)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[18]++;
				break;
				
			case "and":
				temp = stack.pop();
				temp = stack.pop() & temp;
				if(temp == 1)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[19]++;
				break;
				
			case "or":
				temp = stack.pop();
				temp = stack.pop() | temp;
				if(temp == 1)
					stack.push(1);
				else
					stack.push(0);
				InstructionCount[20]++;
				break;
				
			case "swp":
				temp = stack.pop();
				temp2 = stack.pop();
				stack.push(temp);
				stack.push(temp2);
				InstructionCount[21]++;
				break;
				
			case "ldp":
				//load prarameters��� �ǹ̷� �Լ��� �����ڵ��� ���ÿ� �����ϴ°��� ��Ÿ���µ�,
				//�� ��ɾ��� ����� ���ǵ� U-CODE�� �̹� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[22]++;
				break;
			case "ret":
				//��ȯ���� ���� ��ɾ�
				//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[23]++;
				break;
				
			case "retv":
				//��ȯ���� �ִ� ��ɾ�
				//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
				InstructionCount[24]++;
				break;
				
			case "end":
				PC = ReturnStack.pop() - 1;
				if( PC + 1== ProgrameEnd )//main ���ν��� ���������� end��ɾ���, ���α׷� ���Ḧ ��Ÿ��.
				{
					PC = -2;
					System.out.println("���α׷��� ���������� ���� �Ǿ����ϴ�. End Main");
					UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
					InstructionSequence.add("           end");
					UCodeInterpreterFrame.InstrSeqTextArea.append("end \n");
					UCodeInterpreterFrame.ResultTextArea.append("���α׷��� ���������� ����Ǿ����ϴ�. End Main \n");
					StepLine++;
					UCodeInterpreterFrame.StepExecuteSelected = false;
					UCodeInterpreterFrame.LabelExecuteSelected = false;
					return;
				}
				InstructionCount[25]++;
				break;
			//1-address ��ɾ�
			case "bgn":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				InstructionCount[26]++;
				break;
				
			case "ujp":
				String ujpValue1 = instruction.nextToken();
				Iterator<Label> ujpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
				while(ujpLabelTableit.hasNext()){
					Label tableLabel = ujpLabelTableit.next();
					if(ujpValue1.equals(tableLabel.getLabel()))
					{
						PC = tableLabel.getAddress();
						
						break;
					}
					
				}
				InstructionCount[27]++;
				break;
				
			case "tjp":
				String tjpValue1 = instruction.nextToken();
				if(stack.pop() == 1)
				{
					Iterator<Label> tjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(tjpLabelTableit.hasNext()){
						Label tableLabel = tjpLabelTableit.next();
						if(tjpValue1.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress();
							break;
						}
						
					}
				}
				InstructionCount[28]++;
				break;
			case "fjp":
				String fjpValue1 = instruction.nextToken();
				if(stack.pop() == 0)
				{
					Iterator<Label> fjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(fjpLabelTableit.hasNext()){
						Label tableLabel = fjpLabelTableit.next();
						if(fjpValue1.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress();
							break;
						}
						
					}
				}
				InstructionCount[29]++;
				break;
				
			case "ldc":
				int ldcValue1 = Integer.parseInt(instruction.nextToken());
				stack.push(ldcValue1);	//������� �ִ´�.
				InstructionCount[30]++;
				break;
				
			case "call":
				String Label = instruction.nextToken();
				switch(Label)
				{
				case "write":
					int result = stack.pop();
					UCodeInterpreterFrame.ResultTextArea.append(result + "  ");
					StatisticResult = StatisticResult.concat(Integer.toString(result) + " ");
					break;
					
				case "read":
					do
					{
						String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"����� �Է�");
						if(Input == null || Input.equals(""))
						{
							continue;
						}
						else
						{
							int Result = Integer.parseInt(Input);
							UCodeInterpreterFrame.ResultTextArea.append("����� �Է� : " + Result + "\n");
							Memory[stack.pop()] = Result;
							break;
						}
						
					}while(true);
					
					break;
					
				case "lf":
					System.out.println();
					UCodeInterpreterFrame.ResultTextArea.append("\n");
					StatisticResult = StatisticResult.concat("\r\n");
					break;
				case "main":	
					ProgrameEnd = PC+1; //���α׷��� ������Ÿ���� �ּ�
					//-----------------------------------------------------------------------------------------
				default:
					//�������ǵ� �Լ��̸�,�Լ������� �� ���ƿ� �����ּ� ����
					if(Label.substring(0,2) != "$$")
						ReturnStack.push(PC + 1);
					Iterator<Label> LabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(LabelTableit.hasNext()){
						Label tableLabel = LabelTableit.next();
						if(Label.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress() - 1;
							break;
						}
						
					}
				}//End switch
				InstructionCount[31]++;
				break;
				
			//2-address ��ɾ�
			case "lod":
				int lodValue1 = Integer.parseInt(instruction.nextToken());
				int lodValue2 = Integer.parseInt(instruction.nextToken());
				stack.push(Memory[BlockAddress[lodValue1] + lodValue2]);
				InstructionCount[32]++;
				break;
				
			case "lda":
				int ldaValue1 = Integer.parseInt(instruction.nextToken());
				int ldaValue2 = Integer.parseInt(instruction.nextToken());
				stack.push(BlockAddress[ldaValue1] + ldaValue2);
				InstructionCount[33]++;
				break;
				
			case "str":
				int strValue1 = Integer.parseInt(instruction.nextToken());
				int strValue2 = Integer.parseInt(instruction.nextToken());
				Memory[BlockAddress[strValue1] + strValue2] = stack.pop();
				InstructionCount[34]++;
				break;
				
			case "proc":
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
				InstructionCount[35]++;
				break;
			
			//3-address ��ɾ�
			case "sym":	
				//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������(proc ��ɾ� ��ɿ� ���Ե�)
				InstructionCount[36]++;
				break;
				
			default:
				System.out.println("������� �ʴ� ��ɾ��Դϴ�!!!!");
				System.out.println("���α׷��� �����մϴ�...");
				System.exit(0);
				break;
			}//End switch
			
			//��ɾ� ������� ����
			if(PC >= 0)
			{
				InstructionSequence.add(CodeLine.get(PC));
				StringTokenizer st = new StringTokenizer(InstructionBuffer[PC]," ");
				UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
				while(st.hasMoreTokens())
				{
					String token = st.nextToken();
					UCodeInterpreterFrame.InstrSeqTextArea.append(token+"\t");
				}
				
				UCodeInterpreterFrame.InstrSeqTextArea.append("\n");
				StepLine++;
			}


			//������ ���¸� �����ش�.
			stack.view();
			PC++; //���� ��ɾ� ����
		
	}//End StepExecute()
	
	//���̺� ������ ����
	void LableExecute()
	{
		int temp;
		int temp2;
		
			//��ɾ����
			boolean ExitFlag = true;
			while(ExitFlag)
			{
				String Chunk = InstructionBuffer[PC];
				StringTokenizer instruction = new StringTokenizer(Chunk, " ");
				
				switch(instruction.nextToken())
				{
				//0-address ��ɾ�
				case "nop":
					//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
					//�ܼ� ���̺��� ��ġ�� ��Ÿ���Ƿ� �ƹ� ��ɵ� ��������.
					ExitFlag = false;//���̺� ������ �����ϹǷ�, �̺κп��� �ݺ����� ��������.
					InstructionCount[0]++;
					break;
				case "ldi":
					//���� ������� ���� pop�Ͽ� �ּҰ����� ����ϰ�, �����͸� ���ÿ� ����
					temp = stack.pop();
					
					stack.push(Memory[temp]);
					//pause();
					InstructionCount[1]++;
					break;
					
				case "sti":
					//���� �ּҹ��� �̿��� ���� ������� ���� �޸𸮿� �����Ѵ�.
					//������ ������ �ּҿ� ������ ��,�ΰ��� ���ÿ��� pop �ȴ�.
					temp = stack.pop();		//������ ��
					temp2 = stack.pop();	//������ �ּ�
					Memory[temp2] = temp;
	
					InstructionCount[2]++;
					break;
					
				case "not":
					temp = stack.pop();
					if(temp == 1)
						stack.push(0);
					else
						stack.push(1);
					
					InstructionCount[3]++;
					break;
					
				case "neg":
					temp = stack.pop();
					stack.push(-temp);
					InstructionCount[4]++;
					break;
	
				case "inc":
					temp = stack.pop();
					stack.push(++temp);
					InstructionCount[5]++;
					break;
					
				case "dec":
					temp = stack.pop();
					stack.push(--temp);
					InstructionCount[6]++;
					break;
					
				case "dup":
					temp = stack.pop();
					stack.push(temp);
					stack.push(temp);
					InstructionCount[7]++;
					break;
					
				case "add":
					temp = stack.pop();
					stack.push(stack.pop() + temp);
					InstructionCount[8]++;
					break;
					
				case "sub":
					temp = stack.pop();
					stack.push(stack.pop() - temp);
					InstructionCount[9]++;
					break;
				case "mult":
					temp = stack.pop();
					stack.push(stack.pop() * temp);
					InstructionCount[10]++;
					break;
	
				case "div":
					temp = stack.pop();
					stack.push(stack.pop() / temp);
					InstructionCount[11]++;
					break;
					
				case "mod":
					temp = stack.pop();
					stack.push(stack.pop() % temp);
					InstructionCount[12]++;
					break;
					
				case "gt":
					temp = stack.pop();
					temp2 = stack.pop();
					if(temp2 > temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[13]++;
					break;
					
				case "lt":
					temp = stack.pop();
					if(stack.pop() < temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[14]++;
					break;
					
					
				case"ge":
					temp = stack.pop();
					temp2 = stack.pop();
					if(temp2 >= temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[15]++;
					break;
					
				case "le":
					temp = stack.pop();
					if(stack.pop() <= temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[16]++;
					break;
					
				case "eq":
					temp = stack.pop();
					if(stack.pop() == temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[17]++;
					break;
					
				case "ne":
					temp = stack.pop();
					if(stack.pop() != temp)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[18]++;
					break;
					
				case "and":
					temp = stack.pop();
					temp = stack.pop() & temp;
					if(temp == 1)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[19]++;
					break;
					
				case "or":
					temp = stack.pop();
					temp = stack.pop() | temp;
					if(temp == 1)
						stack.push(1);
					else
						stack.push(0);
					InstructionCount[20]++;
					break;
					
				case "swp":
					temp = stack.pop();
					temp2 = stack.pop();
					stack.push(temp);
					stack.push(temp2);
					InstructionCount[21]++;
					break;
					
				case "ldp":
					//load prarameters��� �ǹ̷� �Լ��� �����ڵ��� ���ÿ� �����ϴ°��� ��Ÿ���µ�,
					//�� ��ɾ��� ����� ���ǵ� U-CODE�� �̹� ���Ե� ����̹Ƿ� ���������ʴ´�.
					InstructionCount[22]++;
					break;
				case "ret":
					//��ȯ���� ���� ��ɾ�
					//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
					InstructionCount[23]++;
					break;
					
				case "retv":
					//��ȯ���� �ִ� ��ɾ�
					//���ǵ� U-CODE�� ���Ե� ����̹Ƿ� ���������ʴ´�.
					InstructionCount[24]++;
					break;
					
				case "end":
					PC = ReturnStack.pop() - 1;
					if( PC + 1== ProgrameEnd )//main ���ν��� ���������� end��ɾ���, ���α׷� ���Ḧ ��Ÿ��.
					{
						PC = -2;
						System.out.println("���α׷��� ���������� ���� �Ǿ����ϴ�. End Main");
						UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
						InstructionSequence.add("           end");
						UCodeInterpreterFrame.InstrSeqTextArea.append("end \n");
						UCodeInterpreterFrame.ResultTextArea.append("���α׷��� ���������� ����Ǿ����ϴ�. End Main \n");
						StepLine++;
						UCodeInterpreterFrame.StepExecuteSelected = false;
						UCodeInterpreterFrame.LabelExecuteSelected = false;
						ExitFlag = false;//���̺� ������ ����������, ���α׷��� ���� �����ϸ�, ����
						return;
					}
					InstructionCount[25]++;
					break;
				//1-address ��ɾ�
				case "bgn":
					//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
					InstructionCount[26]++;
					break;
					
				case "ujp":
					String ujpValue1 = instruction.nextToken();
					Iterator<Label> ujpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
					while(ujpLabelTableit.hasNext()){
						Label tableLabel = ujpLabelTableit.next();
						if(ujpValue1.equals(tableLabel.getLabel()))
						{
							PC = tableLabel.getAddress();
							
							break;
						}
						
					}
					InstructionCount[27]++;
					break;
					
				case "tjp":
					String tjpValue1 = instruction.nextToken();
					if(stack.pop() == 1)
					{
						Iterator<Label> tjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
						while(tjpLabelTableit.hasNext()){
							Label tableLabel = tjpLabelTableit.next();
							if(tjpValue1.equals(tableLabel.getLabel()))
							{
								PC = tableLabel.getAddress();
								break;
							}
							
						}
					}
					InstructionCount[28]++;
					break;
				case "fjp":
					String fjpValue1 = instruction.nextToken();
					if(stack.pop() == 0)
					{
						Iterator<Label> fjpLabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
						while(fjpLabelTableit.hasNext()){
							Label tableLabel = fjpLabelTableit.next();
							if(fjpValue1.equals(tableLabel.getLabel()))
							{
								PC = tableLabel.getAddress();
								break;
							}
							
						}
					}
					InstructionCount[29]++;
					break;
					
				case "ldc":
					int ldcValue1 = Integer.parseInt(instruction.nextToken());
					stack.push(ldcValue1);	//������� �ִ´�.
					InstructionCount[30]++;
					break;
					
				case "call":
					String Label = instruction.nextToken();
					switch(Label)
					{
					case "write":
						int result = stack.pop();
						UCodeInterpreterFrame.ResultTextArea.append(result + "  ");
						StatisticResult = StatisticResult.concat(Integer.toString(result) + " ");
						break;
						
					case "read":
						do
						{
							String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"����� �Է�");
							if(Input == null || Input.equals(""))
							{
								continue;
							}
							else
							{
								int Result = Integer.parseInt(Input);
								UCodeInterpreterFrame.ResultTextArea.append("����� �Է� : " + Result + "\n");
								Memory[stack.pop()] = Result;
								break;
							}
							
						}while(true);
						
						break;
						
					case "lf":
						System.out.println();
						UCodeInterpreterFrame.ResultTextArea.append("\n");
						StatisticResult = StatisticResult.concat("\r\n");
						break;
					case "main":	
						ProgrameEnd = PC+1; //���α׷��� ������Ÿ���� �ּ�
						//-----------------------------------------------------------------------------------------
					default:
						//�������ǵ� �Լ��̸�,�Լ������� �� ���ƿ� �����ּ� ����
						if(Label.substring(0,2) != "$$")
							ReturnStack.push(PC + 1);
						Iterator<Label> LabelTableit = LabelTable.iterator();//���̺� ��ü ���(���̺� ����)
						while(LabelTableit.hasNext()){
							Label tableLabel = LabelTableit.next();
							if(Label.equals(tableLabel.getLabel()))
							{
								PC = tableLabel.getAddress() - 1;
								break;
							}
							
						}
					}//End switch
					InstructionCount[31]++;
					break;
					
				//2-address ��ɾ�
				case "lod":
					int lodValue1 = Integer.parseInt(instruction.nextToken());
					int lodValue2 = Integer.parseInt(instruction.nextToken());
					stack.push(Memory[BlockAddress[lodValue1] + lodValue2]);
					InstructionCount[32]++;
					break;
					
				case "lda":
					int ldaValue1 = Integer.parseInt(instruction.nextToken());
					int ldaValue2 = Integer.parseInt(instruction.nextToken());
					stack.push(BlockAddress[ldaValue1] + ldaValue2);
					InstructionCount[33]++;
					break;
					
				case "str":
					int strValue1 = Integer.parseInt(instruction.nextToken());
					int strValue2 = Integer.parseInt(instruction.nextToken());
					Memory[BlockAddress[strValue1] + strValue2] = stack.pop();
					InstructionCount[34]++;
					break;
					
				case "proc":
					//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������
					ExitFlag = false;//���̺� ������ �����ϹǷ�, �̺κп��� �ݺ����� ��������.
					InstructionCount[35]++;
					break;
				
				//3-address ��ɾ�
				case "sym":	
					//����� �ܰ迡�� ����Ǵ� ��ɾ��̹Ƿ�,�������(proc ��ɾ� ��ɿ� ���Ե�)
					InstructionCount[36]++;
					break;
					
				default:
					System.out.println("������� �ʴ� ��ɾ��Դϴ�!!!!");
					System.out.println("���α׷��� �����մϴ�...");
					System.exit(0);
					break;
				}//End switch
				
				//��ɾ� ������� ����
				if(PC >= 0)
				{
					InstructionSequence.add(CodeLine.get(PC));
					StringTokenizer st = new StringTokenizer(InstructionBuffer[PC]," ");
					UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
					while(st.hasMoreTokens())
					{
						String token = st.nextToken();
						UCodeInterpreterFrame.InstrSeqTextArea.append(token+"\t");
					}
					
					UCodeInterpreterFrame.InstrSeqTextArea.append("\n");
					StepLine++;
				}
	
	
				//������ ���¸� �����ش�.
				stack.view();
				PC++; //���� ��ɾ� ����		
			}//End while()
		
		
	}//End LableExecute()

}//End UCodeInterpreter

class Stack {
	public static final int MAX_LEN = 20000;	//������ ũ��(20000)
	int top;	//���� ������
	int Stack[];
	
	public Stack() {
		Stack = new int[MAX_LEN];
		top = 0;
	}
	
	public int length() {
		return top;
	}
	
	public void view(){
		UCodeInterpreterFrame.StackTextArea.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		for(int i=top;i>=1;i--)
		{
			UCodeInterpreterFrame.StackTextArea.append(Stack[i] + "\n");
		}
	}

	public int pop() {
		
		if(top <= 0)
		{
			System.out.println("���� ����÷ο� �߻�!");
			pause(); // '����'��Ű��
			System.exit(1);
		}
		return Stack[top--];
	}

	public boolean push(int ob) {
		
		++top;
		if(top >= MAX_LEN)
		{
			System.out.println("���� �����÷ο� �߻�!");
			pause(); // '����'��Ű��
			System.exit(1);
			
		}
			Stack[top] = ob;
			return true;
		
	}
	
	//�ܼ�â���� �ٷ� ����Ǵ°��� �����ϴ� �Լ�
		public static void pause() {
			try {
			      System.in.read();
			} catch (IOException e) { }
			  

		}//End pause()

}//End Stack