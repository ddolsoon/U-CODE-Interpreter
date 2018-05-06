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
		StackLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		StackLabel.setBounds(646, 10, 75, 26);
		frmUcode.getContentPane().add(StackLabel);
		
		JLabel LabelTableLabel = new JLabel("\uB808\uC774\uBE14 \uD14C\uC774\uBE14");
		LabelTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		LabelTableLabel.setFont(new Font("맑은 고딕", Font.BOLD, 19));
		LabelTableLabel.setBounds(733, 8, 223, 33);
		frmUcode.getContentPane().add(LabelTableLabel);
		
		StepExecuteSelected = false;
		LabelExecuteSelected = false;

		//실행 버튼
		JButton ExecuteButton = new JButton("\uC2E4\uD589");
		ExecuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE를 실행하시겠습니까?","U-CODE 실행",JOptionPane.YES_NO_OPTION);
				//단계별로 실행 중에, 실행을 누를경우가 있으므로, 부분적으로 초기화 해준다.
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
					JOptionPane.showMessageDialog(frmUcode, "파일이 존재하지않습니다!",
							"실행 에러",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("코드를 읽어들이는중............................\n");
				ResultTextArea.append("-------------------------------------------------------------------------------------------------\n");
				ResultTextArea.append("사용된 명령어\n");
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
				ResultTextArea.append("실행 결과 \n");
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
		ExecuteButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ExecuteButton.setBounds(745, 271, 211, 33);
		frmUcode.getContentPane().add(ExecuteButton);
		
		//단계별로 실행 버튼
		JButton StepButton = new JButton("\uB2E8\uACC4\uBCC4\uB85C \uC2E4\uD589");
		StepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StepExecuteSelected == false)
				{
					int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE를 단계별로 실행하시겠습니까?","U-CODE 실행",JOptionPane.YES_NO_OPTION);
				
					if(FileName == null)
					{
						JOptionPane.showMessageDialog(frmUcode, "파일이 존재하지않습니다!",
								"실행 에러",JOptionPane.ERROR_MESSAGE);
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
		StepButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		StepButton.setBounds(745, 313, 211, 33);
		frmUcode.getContentPane().add(StepButton);
		
		//통계파일 생성 버튼
		JButton StatisticButton = new JButton("\uD1B5\uACC4\uD30C\uC77C \uC0DD\uC131");
		StatisticButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(frmUcode,"통계파일(*.lst)를 생성하시겠습니까?",
						"list파일 생성",JOptionPane.YES_NO_OPTION);
				if(FileName == null)
				{
					JOptionPane.showMessageDialog(frmUcode, "파일이 존재하지않습니다!",
							"실행 에러",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(result == JOptionPane.YES_OPTION)
				{
					interpreter.statistic(FileName);
					JOptionPane.showMessageDialog(frmUcode, "통계파일(*.lst이 생성되었습니다.",
							"통계파일 생성",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					return;
				}
			}
		});
		StatisticButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		StatisticButton.setBounds(745, 399, 211, 33);
		frmUcode.getContentPane().add(StatisticButton);
		
		JLabel InstructionSquenceLabel = new JLabel("\uBA85\uB839\uC5B4 \uC2E4\uD589 \uACFC\uC815");
		InstructionSquenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		InstructionSquenceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		InstructionSquenceLabel.setBounds(0, 6, 621, 33);
		frmUcode.getContentPane().add(InstructionSquenceLabel);
		
		JLabel ResultLabel = new JLabel("\uC2E4\uD589 \uACB0\uACFC");
		ResultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		ResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ResultLabel.setBounds(20, 421, 712, 33);
		frmUcode.getContentPane().add(ResultLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 46, 611, 372);
		frmUcode.getContentPane().add(scrollPane);
		
		//명령어 순서 창
		InstrSeqTextArea = new JTextArea();
		InstrSeqTextArea.setEditable(false);
		InstrSeqTextArea.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		scrollPane.setViewportView(InstrSeqTextArea);
		InstrSeqTextArea.append("Line\tInstruction\tOperand1\tOperand2\tOperand3\n");
		InstrSeqTextArea.append("-----------------------------------------------------"
				+ "---------------------------------\n");

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(646, 46, 75, 372);
		frmUcode.getContentPane().add(scrollPane_1);
		
		//스택 상태 창
		StackTextArea = new JTextArea();
		StackTextArea.setEditable(false);
		StackTextArea.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		scrollPane_1.setViewportView(StackTextArea);
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 457, 720, 247);
		frmUcode.getContentPane().add(scrollPane_2);
		
		//실행 결과 창
		ResultTextArea = new JTextArea();
		scrollPane_2.setViewportView(ResultTextArea);
		ResultTextArea.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		ResultTextArea.setForeground(new Color(0, 0, 0));
		ResultTextArea.setEditable(false);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(733, 53, 223, 208);
		frmUcode.getContentPane().add(scrollPane_3);
		
		//레이블 테이블 창
		JTextArea LableTableTextArea = new JTextArea();
		scrollPane_3.setViewportView(LableTableTextArea);
		LableTableTextArea.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		LableTableTextArea.setForeground(Color.BLACK);
		LableTableTextArea.append("Address\tLable");
		LableTableTextArea.append("\n-------------------------------\n");
		
		LableTableTextArea.setEditable(false);
		
		//파일 불러오기 버튼
		JButton FileLoadButton = new JButton("\uD30C\uC77C \uBD88\uB7EC\uC624\uAE30");
		FileLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser FileOpenManager = new JFileChooser("C:\\Users\\Administrator\\Desktop");
				FileOpenManager.setFileFilter(new FileNameExtensionFilter("*.uco", "uco"));
				//새로 불러올 경우, 모두 실행 관련 변수 초기화------------------------------------
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
				
	      		FileOpenManager.showDialog(frmUcode,"U-CODE 불러오기");
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
		
		FileLoadButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		FileLoadButton.setBounds(745, 442, 211, 33);
		frmUcode.getContentPane().add(FileLoadButton);
		
		JLabel MakerLabel = new JLabel("\uC81C\uC791\uC790 : \uAE40\uBBFC\uC131");
		MakerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		MakerLabel.setBounds(763, 490, 178, 33);
		frmUcode.getContentPane().add(MakerLabel);
		
		ImageIcon imageIcon = new ImageIcon("images/watermark.png");
		JLabel WaterMarkLabel = new JLabel(imageIcon);
		WaterMarkLabel.setBounds(745, 533, 211, 157);
		frmUcode.getContentPane().add(WaterMarkLabel);
		
		//레이블 단위로 실행 버튼
		JButton LabelExecuteButton = new JButton("\uB808\uC774\uBE14 \uB2E8\uC704\uB85C \uC2E4\uD589");
		LabelExecuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(StepExecuteSelected == false)
				{
					int result = JOptionPane.showConfirmDialog(frmUcode,"U-CODE를 레이블 단위로 실행하시겠습니까?","U-CODE 실행",JOptionPane.YES_NO_OPTION);
				
					if(FileName == null)
					{
						JOptionPane.showMessageDialog(frmUcode, "파일이 존재하지않습니다!",
								"실행 에러",JOptionPane.ERROR_MESSAGE);
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
		LabelExecuteButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		LabelExecuteButton.setBounds(745, 356, 211, 33);
		frmUcode.getContentPane().add(LabelExecuteButton);
	}
}

/*
	명령어 구성도
	nop,bgn,sym,						//프로그램 구성 명령
	lod,lda,ldc,str,ldi,sti,			// 데이터 이동 연산자
	not,neg,inc,dec,dup,				//단항 연산자
	add,sub,mult,div,mod,				//이항 연산자(산술)
	gt,lt,ge,le,eq,ne,and,or,			//이항 연산자(논리)
	swp,								//이항 연산자(Swap)
	ujp,tjp,fjp,						//흐름 제어
	call,ret,retv,ldp,proc,end			//함수 정의 및 호출
	
}//총 명령어 수 : 37개 사용
*/

//Label 클래스
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
	 int [] Memory;			//메모리
	 int PC;				//프로그램 카운터
	 int [] BlockAddress;	//메모리 블록 시작주소
	 Stack stack;			//스택(임시저장 기억 장치)

	 Vector<String> CodeLine = new Vector<String>();
	 Vector<Label> LabelTable = new Vector<Label>();
	 String [] InstructionBuffer;
	 
	 Stack ReturnStack;	//복귀주소 저장 스택
	 int ProgrameEnd;	//프로그램의 끝
	 
	 
	 
	 //통계에 사용할 변수
	 String StatisticResult;	//실행결과
	 Vector<String> InstructionSequence = new Vector<String>();	//명령어 실행순서
	//명령어 별 이름
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
	 
	 //명령어 별 사이클(순서는 이름과 동일)
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
			 
			 
	 
	 int [] InstructionCount;	//명령어 별 실행횟수
	 int AllInstructionCount;	//실행된 명령어 총 횟수
	 int MemoryAccessCount;		//메모리 접근횟수 lod,lda,str,sti,ldi
	 long AllInstructionCycle; //실행하는데 사용된 총 사이클 수
	 
	 int StepLine;
	 
	public UCodeInterpreter() {
		// TODO Auto-generated constructor stub
		Memory = new int[2000];
		BlockAddress = new int[200];
		stack = new Stack();
		ReturnStack = new Stack();
		StepLine = 1;
		//모든 처음 블록주소는 0으로 초기화.
		for(int i=0;i<BlockAddress.length; i++)
			BlockAddress[i] = 0;
		//이전 블록의 사이즈를 더하면,다음 블록의 시작주소
		
		
		InstructionBuffer = new String[2000];
		
		PC = 0;
		InstructionCount = new int[37];
		/*
		 * 명령어 순서
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
			InstructionCount[i] = 0; //모든 초기 실행횟수 0으로 초기화
		
		StatisticResult = "";
		MemoryAccessCount = 0;
		AllInstructionCount = 0;
		AllInstructionCycle = 0;
		
	}//End UCodeInterpreter()
	
	void readUcode(String FileName)
	{
		
		//----------------전체코드 읽어오기-----------------------
		try {
			
			Scanner fileScanner = new Scanner(new FileReader(FileName));
					
			while(fileScanner.hasNext()){
				CodeLine.add(fileScanner.nextLine());	//한 라인씩 읽어서 저장
			}
			
			fileScanner.close();
		} catch (FileNotFoundException  e) {
			// TODO: handle exception
			System.out.println("파일 열기 실패 오류");
		}
		//---------------------------------------------------------
		
	}//End readUcode(String FileName)
	
	void assemble(){
		
		System.out.println("레이블 테이블");
		System.out.println("-------------------------------------------------------");
		//레이블 추출---------------------------------------------------
		Iterator<String> codeit = CodeLine.iterator();//Iterator 객체 얻기(레이블 추출)
		int Line = 0;
		while(codeit.hasNext()){
			
			//레이블 추출하기
			String Label = codeit.next();
			Label = Label.substring(0,10);	//레이블만 추출
			Label = Label.trim();	//공백 제거
			if(!Label.equals(""))//공백이 아니라면, 레이블 테이블에 저장
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
		System.out.println("사용된 명령어");
		System.out.println("-------------------------------------------------------");
		//명령어 추출하기---------------------------------------------------------------
		Iterator<String> Insit = CodeLine.iterator();//Iterator 객체 얻기(명령어 추출)
		char comment = 0;
		int j = 0;
		while(Insit.hasNext()){
			String Instruction = Insit.next();
			
			int i;
			i=0;
			comment = 0;
			for(;i<Instruction.length();i++)
			{	
				//주석부분은 건너뜀
				if(Instruction.charAt(i) == '%')
				{
					comment = 1;
					break;
				}
			}
			if(comment == 1)	//주석이있다면, 주석 전까지만 명령어 추출
				Instruction = Instruction.substring(11,i-1);
			else				//주석이없다면, 전체 명령어 추출
				Instruction = Instruction.substring(11);
			
			InstructionBuffer[j++] = Instruction;
			
		}
		//----------------------------------------------------------------------------
		
		
		
		//사용된 명령어 출력
		for(int i=0; i < j ;i++)
			System.out.println(i + " " + InstructionBuffer[i]);
		
		//메모리 블록 할당(bgn,proc)---------------------------------------------------------------
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
				//다음 블록의 시작주소를 지정
				int procvalue1 = Integer.parseInt(instruction.nextToken());	//블록 번호
				int procvalue2 = Integer.parseInt(instruction.nextToken());	//블록 사이즈
				//이전 블록의 시작주소를 불러온다.
				BlockAddress[procvalue1 + 1] = BlockAddress[procvalue1];
				//블록의 사이즈만큼 더한다.
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
		//명령어실행
		while(PC >= 0)
		{
			String Chunk = InstructionBuffer[PC];
			StringTokenizer instruction = new StringTokenizer(Chunk, " ");
			
			switch(instruction.nextToken())
			{
			//0-address 명령어
			case "nop":
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				//단순 레이블의 위치를 나타내므로 아무 기능도 하지않음.
				InstructionCount[0]++;
				break;
			case "ldi":
				//스택 꼭대기의 값을 pop하여 주소값으로 사용하고, 데이터를 스택에 저장
				temp = stack.pop();
				
				stack.push(Memory[temp]);
				//pause();
				InstructionCount[1]++;
				break;
				
			case "sti":
				//간접 주소법을 이용해 스택 꼭대기의 값을 메모리에 저장한다.
				//저장할 변수의 주소와 저장할 값,두개가 스택에서 pop 된다.
				temp = stack.pop();		//저장할 값
				temp2 = stack.pop();	//저장할 주소
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
				//load prarameters라는 의미로 함수의 실인자들을 스택에 저장하는것을 나타내는데,
				//이 명령어의 기능이 정의된 U-CODE에 이미 포함된 기능이므로 구현하지않는다.
				InstructionCount[22]++;
				break;
			case "ret":
				//반환값이 없는 명령어
				//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
				InstructionCount[23]++;
				break;
				
			case "retv":
				//반환값이 있는 명령어
				//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
				InstructionCount[24]++;
				break;
				
			case "end":
				PC = ReturnStack.pop() - 1;
				if( PC + 1== ProgrameEnd )//main 프로시저 종료이후의 end명령어라면, 프로그램 종료를 나타냄.
				{
					PC = -2;
					System.out.println("프로그램이 정상적으로 종료 되었습니다. End Main");
				}
				InstructionCount[25]++;
				break;
			//1-address 명령어
			case "bgn":
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				InstructionCount[26]++;
				break;
				
			case "ujp":
				String ujpValue1 = instruction.nextToken();
				Iterator<Label> ujpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					Iterator<Label> tjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					Iterator<Label> fjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
				stack.push(ldcValue1);	//상수값을 넣는다.
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
						String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"사용자 입력");
						if(Input == null || Input.equals(""))
						{
							continue;
						}
						else
						{
							int Result = Integer.parseInt(Input);
							UCodeInterpreterFrame.ResultTextArea.append("사용자 입력 : " + Result + "\n");
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
					ProgrameEnd = PC+1; //프로그램의 끝을나타내는 주소
					//-----------------------------------------------------------------------------------------
				default:
					//새로정의된 함수이면,함수가끝난 후 돌아올 복귀주소 저장
					if(Label.substring(0,2) != "$$")
						ReturnStack.push(PC + 1);
					Iterator<Label> LabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
				
			//2-address 명령어
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
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				InstructionCount[35]++;
				break;
			
			//3-address 명령어
			case "sym":	
				//어셈블 단계에서 실행되는 명령어이므로,내용없음(proc 명령어 기능에 포함됨)
				InstructionCount[36]++;
				break;
				
			default:
				System.out.println("취급하지 않는 명령어입니다!!!!");
				System.out.println("프로그램을 종료합니다...");
				System.exit(0);
				break;
			}//End switch
			
			//명령어 실행순서 저장
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
			PC++; //다음 명령어 실행
		}//End While

	}//End execute()
	
	void statistic(String FileName)
	{
		//통계파일 출력 메소드
		int index = FileName.lastIndexOf(".uco");
		FileName = FileName.substring(0,index);
		String StatisticName = FileName.concat(".lst");
		
		try {
			PrintWriter filewriter = new PrintWriter (StatisticName);
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("**********************************사용된 UCODE**********************************\r\n");
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
			filewriter.printf("********************************명령어 실행순서********************************\r\n");
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
			filewriter.printf("********************************실행 결과********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("%s \r\n", StatisticResult);
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("***********************************명령어 별 실행 횟수************************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");			
			for(int i=0;i<37;i++)
			{
				filewriter.printf("%s\t%d\t",InstructionName[i],InstructionCount[i]);
				if((i+1)%5 == 0)
					filewriter.printf("\r\n");
				
			}
			filewriter.printf("\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			filewriter.printf("********************************메모리 접근 횟수********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");			
			MemoryAccessCount += InstructionCount[1];
			MemoryAccessCount += InstructionCount [2];
			MemoryAccessCount += InstructionCount [32];
			MemoryAccessCount += InstructionCount [33];	
			MemoryAccessCount += InstructionCount [34];
			filewriter.printf("메모리 접근 명령어 : ldi,sti,lod,lda,str \r\n");
			filewriter.printf("메모리 접근 횟수 : %d \r\n",MemoryAccessCount);
			filewriter.printf("---------------------------------------------------------------------------------\r\n");			
			filewriter.printf("********************************총 명령어 실행 횟수********************************\r\n");
			filewriter.printf("---------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
				AllInstructionCount += InstructionCount[i];
			filewriter.printf("총 명령어 실행 횟수 : %d \r\n",AllInstructionCount);
			
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("***********************************명령어 별 사용 비율************************************\r\n");
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
			filewriter.printf("*******************************명령어 별 소요된 사이클 수********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
			{
				filewriter.printf("%s\t%d Cycle\t",InstructionName[i],InstructionCycle[i]*InstructionCount[i]);
				if((i+1)%1 == 0)
					filewriter.printf("\r\n");
			}
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************명령어에 사용된 총 사이클 수********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			for(int i=0;i<37;i++)
				AllInstructionCycle += InstructionCycle[i]*InstructionCount[i];
			filewriter.printf("프로그램을 실행하는데 사용된 총 사이클 수 : %d Cycle\r\n",AllInstructionCycle);
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************부                        록********************************\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("-------------------------------------------------------------------------------------------\r\n");
			filewriter.printf("*******************************명령어 별 필요한 사이클 수********************************\r\n");
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
			System.out.println("파일 입출력 오류 발생!");
			pause();
			System.exit(1);
		}
		
	}
		
	//콘솔창에서 바로 종료되는것을 방지하는 함수
	public static void pause() {
		try {
		      System.in.read();
		} catch (IOException e) { }
		  

	}//End pause()
	
	//단계별로 실행하는 함수
	void StepExecute()
	{
		int temp;
		int temp2;
		//명령어실행
			String Chunk = InstructionBuffer[PC];
			StringTokenizer instruction = new StringTokenizer(Chunk, " ");
			
			switch(instruction.nextToken())
			{
			//0-address 명령어
			case "nop":
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				//단순 레이블의 위치를 나타내므로 아무 기능도 하지않음.
				InstructionCount[0]++;
				break;
			case "ldi":
				//스택 꼭대기의 값을 pop하여 주소값으로 사용하고, 데이터를 스택에 저장
				temp = stack.pop();
				
				stack.push(Memory[temp]);
				//pause();
				InstructionCount[1]++;
				break;
				
			case "sti":
				//간접 주소법을 이용해 스택 꼭대기의 값을 메모리에 저장한다.
				//저장할 변수의 주소와 저장할 값,두개가 스택에서 pop 된다.
				temp = stack.pop();		//저장할 값
				temp2 = stack.pop();	//저장할 주소
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
				//load prarameters라는 의미로 함수의 실인자들을 스택에 저장하는것을 나타내는데,
				//이 명령어의 기능이 정의된 U-CODE에 이미 포함된 기능이므로 구현하지않는다.
				InstructionCount[22]++;
				break;
			case "ret":
				//반환값이 없는 명령어
				//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
				InstructionCount[23]++;
				break;
				
			case "retv":
				//반환값이 있는 명령어
				//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
				InstructionCount[24]++;
				break;
				
			case "end":
				PC = ReturnStack.pop() - 1;
				if( PC + 1== ProgrameEnd )//main 프로시저 종료이후의 end명령어라면, 프로그램 종료를 나타냄.
				{
					PC = -2;
					System.out.println("프로그램이 정상적으로 종료 되었습니다. End Main");
					UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
					InstructionSequence.add("           end");
					UCodeInterpreterFrame.InstrSeqTextArea.append("end \n");
					UCodeInterpreterFrame.ResultTextArea.append("프로그램이 정상적으로 종료되었습니다. End Main \n");
					StepLine++;
					UCodeInterpreterFrame.StepExecuteSelected = false;
					UCodeInterpreterFrame.LabelExecuteSelected = false;
					return;
				}
				InstructionCount[25]++;
				break;
			//1-address 명령어
			case "bgn":
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				InstructionCount[26]++;
				break;
				
			case "ujp":
				String ujpValue1 = instruction.nextToken();
				Iterator<Label> ujpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					Iterator<Label> tjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					Iterator<Label> fjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
				stack.push(ldcValue1);	//상수값을 넣는다.
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
						String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"사용자 입력");
						if(Input == null || Input.equals(""))
						{
							continue;
						}
						else
						{
							int Result = Integer.parseInt(Input);
							UCodeInterpreterFrame.ResultTextArea.append("사용자 입력 : " + Result + "\n");
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
					ProgrameEnd = PC+1; //프로그램의 끝을나타내는 주소
					//-----------------------------------------------------------------------------------------
				default:
					//새로정의된 함수이면,함수가끝난 후 돌아올 복귀주소 저장
					if(Label.substring(0,2) != "$$")
						ReturnStack.push(PC + 1);
					Iterator<Label> LabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
				
			//2-address 명령어
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
				//어셈블 단계에서 실행되는 명령어이므로,내용없음
				InstructionCount[35]++;
				break;
			
			//3-address 명령어
			case "sym":	
				//어셈블 단계에서 실행되는 명령어이므로,내용없음(proc 명령어 기능에 포함됨)
				InstructionCount[36]++;
				break;
				
			default:
				System.out.println("취급하지 않는 명령어입니다!!!!");
				System.out.println("프로그램을 종료합니다...");
				System.exit(0);
				break;
			}//End switch
			
			//명령어 실행순서 저장
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


			//스택의 상태를 보여준다.
			stack.view();
			PC++; //다음 명령어 실행
		
	}//End StepExecute()
	
	//레이블 단위로 실행
	void LableExecute()
	{
		int temp;
		int temp2;
		
			//명령어실행
			boolean ExitFlag = true;
			while(ExitFlag)
			{
				String Chunk = InstructionBuffer[PC];
				StringTokenizer instruction = new StringTokenizer(Chunk, " ");
				
				switch(instruction.nextToken())
				{
				//0-address 명령어
				case "nop":
					//어셈블 단계에서 실행되는 명령어이므로,내용없음
					//단순 레이블의 위치를 나타내므로 아무 기능도 하지않음.
					ExitFlag = false;//레이블 단위로 실행하므로, 이부분에서 반복문을 빠져나옴.
					InstructionCount[0]++;
					break;
				case "ldi":
					//스택 꼭대기의 값을 pop하여 주소값으로 사용하고, 데이터를 스택에 저장
					temp = stack.pop();
					
					stack.push(Memory[temp]);
					//pause();
					InstructionCount[1]++;
					break;
					
				case "sti":
					//간접 주소법을 이용해 스택 꼭대기의 값을 메모리에 저장한다.
					//저장할 변수의 주소와 저장할 값,두개가 스택에서 pop 된다.
					temp = stack.pop();		//저장할 값
					temp2 = stack.pop();	//저장할 주소
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
					//load prarameters라는 의미로 함수의 실인자들을 스택에 저장하는것을 나타내는데,
					//이 명령어의 기능이 정의된 U-CODE에 이미 포함된 기능이므로 구현하지않는다.
					InstructionCount[22]++;
					break;
				case "ret":
					//반환값이 없는 명령어
					//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
					InstructionCount[23]++;
					break;
					
				case "retv":
					//반환값이 있는 명령어
					//정의된 U-CODE에 포함된 기능이므로 구현하지않는다.
					InstructionCount[24]++;
					break;
					
				case "end":
					PC = ReturnStack.pop() - 1;
					if( PC + 1== ProgrameEnd )//main 프로시저 종료이후의 end명령어라면, 프로그램 종료를 나타냄.
					{
						PC = -2;
						System.out.println("프로그램이 정상적으로 종료 되었습니다. End Main");
						UCodeInterpreterFrame.InstrSeqTextArea.append(StepLine+"\t");
						InstructionSequence.add("           end");
						UCodeInterpreterFrame.InstrSeqTextArea.append("end \n");
						UCodeInterpreterFrame.ResultTextArea.append("프로그램이 정상적으로 종료되었습니다. End Main \n");
						StepLine++;
						UCodeInterpreterFrame.StepExecuteSelected = false;
						UCodeInterpreterFrame.LabelExecuteSelected = false;
						ExitFlag = false;//레이블 단위로 실행하지만, 프로그램의 끝에 도달하면, 종료
						return;
					}
					InstructionCount[25]++;
					break;
				//1-address 명령어
				case "bgn":
					//어셈블 단계에서 실행되는 명령어이므로,내용없음
					InstructionCount[26]++;
					break;
					
				case "ujp":
					String ujpValue1 = instruction.nextToken();
					Iterator<Label> ujpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
						Iterator<Label> tjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
						Iterator<Label> fjpLabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					stack.push(ldcValue1);	//상수값을 넣는다.
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
							String Input = JOptionPane.showInputDialog(UCodeInterpreterFrame.frmUcode,"사용자 입력");
							if(Input == null || Input.equals(""))
							{
								continue;
							}
							else
							{
								int Result = Integer.parseInt(Input);
								UCodeInterpreterFrame.ResultTextArea.append("사용자 입력 : " + Result + "\n");
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
						ProgrameEnd = PC+1; //프로그램의 끝을나타내는 주소
						//-----------------------------------------------------------------------------------------
					default:
						//새로정의된 함수이면,함수가끝난 후 돌아올 복귀주소 저장
						if(Label.substring(0,2) != "$$")
							ReturnStack.push(PC + 1);
						Iterator<Label> LabelTableit = LabelTable.iterator();//레이블 객체 얻기(레이블 추출)
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
					
				//2-address 명령어
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
					//어셈블 단계에서 실행되는 명령어이므로,내용없음
					ExitFlag = false;//레이블 단위로 실행하므로, 이부분에서 반복문을 빠져나옴.
					InstructionCount[35]++;
					break;
				
				//3-address 명령어
				case "sym":	
					//어셈블 단계에서 실행되는 명령어이므로,내용없음(proc 명령어 기능에 포함됨)
					InstructionCount[36]++;
					break;
					
				default:
					System.out.println("취급하지 않는 명령어입니다!!!!");
					System.out.println("프로그램을 종료합니다...");
					System.exit(0);
					break;
				}//End switch
				
				//명령어 실행순서 저장
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
	
	
				//스택의 상태를 보여준다.
				stack.view();
				PC++; //다음 명령어 실행		
			}//End while()
		
		
	}//End LableExecute()

}//End UCodeInterpreter

class Stack {
	public static final int MAX_LEN = 20000;	//스택의 크기(20000)
	int top;	//스택 포인터
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
			System.out.println("스택 언더플로우 발생!");
			pause(); // '중지'시키기
			System.exit(1);
		}
		return Stack[top--];
	}

	public boolean push(int ob) {
		
		++top;
		if(top >= MAX_LEN)
		{
			System.out.println("스택 오버플로우 발생!");
			pause(); // '중지'시키기
			System.exit(1);
			
		}
			Stack[top] = ob;
			return true;
		
	}
	
	//콘솔창에서 바로 종료되는것을 방지하는 함수
		public static void pause() {
			try {
			      System.in.read();
			} catch (IOException e) { }
			  

		}//End pause()

}//End Stack