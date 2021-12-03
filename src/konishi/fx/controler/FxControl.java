package konishi.fx.controler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class FxControl {
	// テストする音声の数
	private static final int TEST_N = 10;
	
	private static final String[] HUMAN_ID = {"A", "B", "C", "D", "E"};
	private static final String DIR = "/home/konishi/workspace_java/HumanRecognitionTester/src/konishi/fx/controler/data/";
	private static final String THANKS_MSG = "テストにご協力いただき、ありがとうございました！";
	private static final String NOT_ALL_ANSWERING_QUESTION_ERROR_MSG = "全ての選択肢に答えてください";
	private static final String NOT_INPUT_NAME_ERROR_MSG = "名前を入力してください";
	
	@FXML private ImageView trainA,trainB,trainC,trainD,trainE;
	@FXML private Label train_labelA,train_labelB,train_labelC,train_labelD,train_labelE;
	
	@FXML private ImageView test1,test2,test3,test4,test5,test6,test7,test8,test9,test10;
	@FXML private ChoiceBox test_choice1,test_choice2,test_choice3,
							test_choice4,test_choice5,test_choice6,
							test_choice7,test_choice8,test_choice9,test_choice10;
	@FXML private Label test_label1,test_label2,test_label3,test_label4,test_label5,test_label6,test_label7,test_label8,test_label9,test_label10;
	
	@FXML private Button submit;
	@FXML private TextField user;
	@FXML private Button write;
	@FXML private Button clear;
	@FXML private Label accuracy_label;
	
	@FXML private Label thanks;
	
	private Label[] trainLabel;
	private ChoiceBox[] testChoice;
	private Label[] testLabel;
	
	
	private String[] name = {"konishi","nakashima","shimada","yamasaki","kamuro"};
	
	private String[] trainList;
	private String[] testList;
	private int[] answer;
	
	private Random random;
	
	private int accuracy;
	private int[] result;
	
	private SoundPlayer sp;
	
	public void initialize() {
		trainLabel = new Label[name.length];
		testChoice = new ChoiceBox[TEST_N];
		testLabel = new Label[TEST_N];
		trainList = new String[name.length];
		testList = new String[TEST_N];
		answer = new int[TEST_N];
		random = new Random();
		result = new int[name.length];
		
		thanks.setFont(new Font(30));
		thanks.setText("");
		accuracy_label.setText("○○ %");
		accuracy_label.setFont(new Font(30));
		
		user.setText("");
		
		ImageView[] trainButton = {trainA,trainB,trainC,trainD,trainE};
		
		ImageView[] testButton = {test1,test2,test3,test4,test5,test6,test7,test8,test9,test10};
		
		Label[] tl = {
				train_labelA,train_labelB,train_labelC,train_labelD,train_labelE
		};
		
		Label[] l = {
				test_label1,test_label2,test_label3,
				test_label4,test_label5,test_label6,
				test_label7,test_label8,test_label9,test_label10
		};
		ChoiceBox[] c = {
				test_choice1,test_choice2,test_choice3,
				test_choice4,test_choice5,test_choice6,
				test_choice7,test_choice8,test_choice9,test_choice10
		};
		
		for (int i = 0; i < TEST_N; i++) {
			testChoice[i] = c[i];
			testLabel[i] = l[i];
		}
		for (int i = 0; i < name.length; i++) {
			trainList[i] = DIR+name[i]+"0"+(random.nextInt(9)+1)+".wav";
			trainLabel[i] = tl[i];
			tl[i].setText(HUMAN_ID[i]);
			tl[i].setFont(new Font(31));
		}
		
		for (ChoiceBox choice : testChoice) {
			choice.setItems(FXCollections.observableArrayList(HUMAN_ID));
		}
		
		for (Label label : testLabel) {
			label.setText("");
		}
		
		/*
		 * 乱数でテストデータ決定
		 */
		for (int i = 0; i < TEST_N; i++) {
			answer[i] = random.nextInt(5);
			testList[i] = DIR + name[answer[i]] + "1" + random.nextInt(TEST_N) + ".wav";
		}
	}
	
	@FXML public void handleSubmitButton(MouseEvent mouse) {
		int count = 0;
		
		int[] total = new int[name.length];
		int[] correct = new int [name.length];
		
		// 提出条件を満たしているかチェック&警告
		for (int i = 0; i < TEST_N; i++) {
			if ((String)testChoice[i].getValue() == null) {
				thanks.setText(NOT_ALL_ANSWERING_QUESTION_ERROR_MSG);
				return;
			}
		}
		if (user.getText().equals("")) {
			thanks.setText(NOT_INPUT_NAME_ERROR_MSG);
			return;
		}
		
		for (int i = 0; i < TEST_N; i++) {
			String selectedID = (String)testChoice[i].getValue();
			String ansID = HUMAN_ID[answer[i]];
			if (ansID.equals(selectedID)) {
				count ++;
				testLabel[i].setText("OK: " + ansID);
			} else {
				testLabel[i].setText("NG: " + ansID);
			}
			
			// 人別識別率
			for (int j = 0; j < name.length; j++) {
				if (answer[i] == j) {
					total[j] ++;
					if (ansID.equals(selectedID)) {
						correct[j] ++;
					}
				}
			}
		}
		for (int i = 0; i < name.length; i++) {
			trainLabel[i].setFont(new Font(20));
			trainLabel[i].setText(HUMAN_ID[i] + " = " + name[i]);
		}
		
		accuracy = count * 10;
		accuracy_label.setText(accuracy + "%");
		thanks.setText(THANKS_MSG);
		
		System.out.print(user.getText() + " ");
		System.out.print(accuracy + "% ");
		for (int i = 0; i < name.length; i++) {
			result[i] = total[i]!=0 ? correct[i]*100/total[i] : 0;
			System.out.print(result[i]+" ");
		}
		System.out.println("");
		
		
	}
	
	/**
	 * ファイル書き込み
	 * @param mouse
	 * @throws IOException
	 */
	@FXML public void handleWriteButton(MouseEvent mouse) throws IOException {
		
		// 回答条件が満たされていない
		if (!thanks.getText().equals(THANKS_MSG)) {
			return;
		}
		
		File file = new File("/home/konishi/Desktop/human_data/data.txt");
		FileWriter filewriter = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(filewriter);
		
		bw.write(user.getText()+",");
		bw.write(Integer.toString(accuracy));
		for (int i = 0; i < name.length; i++) {
			bw.write(",");
			bw.write(Integer.toString(result[i]));
			
		}
		bw.newLine();
		bw.close();
		
		initialize();
	}
	
	/**
	 * ファイル書き込み
	 * @param mouse
	 */
	@FXML public void handleClearButton(MouseEvent mouse) {
		initialize();
	}

	
	@FXML public void handleButtonPressed(MouseEvent mouse) throws Exception  {
		String id = getId(mouse.toString());
		
		if (sp != null) {
			sp.stopThread();
			System.out.println("stop");
		}
		
		switch (id) {
		case "trainA":
			System.out.println("pressed!");
			sp = new SoundPlayer(trainList[0]);
			break;
		case "trainB":
			System.out.println("pressed!");
			sp = new SoundPlayer(trainList[1]);
			break;
		case "trainC":
			System.out.println("pressed!");
			sp = new SoundPlayer(trainList[2]);
			break;
		case "trainD":
			System.out.println("pressed!");
			sp = new SoundPlayer(trainList[3]);
			break;
		case "trainE":
			System.out.println("pressed!");
			sp = new SoundPlayer(trainList[4]);
			break;
			
		case "test1":
			sp = new SoundPlayer(testList[0]);
			break;
		case "test2":
			sp = new SoundPlayer(testList[1]);
			break;
		case "test3":
			sp = new SoundPlayer(testList[2]);
			break;
		case "test4":
			sp = new SoundPlayer(testList[3]);
			break;
		case "test5":
			sp = new SoundPlayer(testList[4]);
			break;
		case "test6":
			sp = new SoundPlayer(testList[5]);
			break;
		case "test7":
			sp = new SoundPlayer(testList[6]);
			break;
		case "test8":
			sp = new SoundPlayer(testList[7]);
			break;
		case "test9":
			sp = new SoundPlayer(testList[8]);
			break;
		case "test10":
			sp = new SoundPlayer(testList[9]);
			break;
		}
		sp.play();
	}
	
	
	
	/**
	 * 重複しない乱数の生成(使い方はnextInt(int num)と同じ)
	 * @param n
	 * @return
	 */
	public ArrayList<Integer> notOverlapRandom(int n) {
		ArrayList<Integer> list = new ArrayList<Integer>();
        for ( int i = 0; i < n; i++ ) {
          list.add(i);
        }
        Collections.shuffle(list);
        for(int i = 0; i < n; i++){
            System.out.println(list.get(i));
        }
        
        return list;
	}
	
	public String getId(String eventString) {
		String idName = "";
		String regex = "id=(.*),";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(eventString);
		if(m.find()) idName = m.group(1);
		
		StringTokenizer st = new StringTokenizer(idName, ",");
		if (st.hasMoreTokens())
			idName = st.nextToken();
		if (idName.endsWith("]"))
			idName = idName.replace("]", "");
		
		return idName;
	}
}
