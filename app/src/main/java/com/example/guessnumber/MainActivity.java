package com.example.guessnumber;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initGame();
        initListView();
    }

    private final int[] inputRes = {R.id.main_input1, R.id.main_input2, R.id.main_input3, R.id.main_input4};
    private final TextView[] input = new TextView[4];
    private final int[] numberRes = {R.id.main_btn_0, R.id.main_btn_1, R.id.main_btn_2, R.id.main_btn_3, R.id.main_btn_4, R.id.main_btn_5, R.id.main_btn_6,
            R.id.main_btn_7, R.id.main_btn_8, R.id.main_btn_9};
    private final Button[] btnNumber = new Button[10];
    //初始化畫面
    private void initView(){
        for (int i = 0; i < inputRes.length; i++){
            input[i] = findViewById(inputRes[i]);
        }

        for (int i = 0; i < numberRes.length; i++){
            btnNumber[i] = findViewById(numberRes[i]);
            btnNumber[i].setOnClickListener(this::onClick);
        }
    }

    private List<Integer> anser = new ArrayList<>();
    //初始化遊戲
    private void initGame(){
        anser = createAnswer();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.main_btn_0){
            inputNumber(0);
        } else if (id == R.id.main_btn_1) {
            inputNumber(1);
        } else if (id == R.id.main_btn_2) {
            inputNumber(2);
        } else if (id == R.id.main_btn_3) {
            inputNumber(3);
        } else if (id == R.id.main_btn_4) {
            inputNumber(4);
        } else if (id == R.id.main_btn_5) {
            inputNumber(5);
        } else if (id == R.id.main_btn_6) {
            inputNumber(6);
        } else if (id == R.id.main_btn_7) {
            inputNumber(7);
        } else if (id == R.id.main_btn_8) {
            inputNumber(8);
        } else if (id == R.id.main_btn_9) {
            inputNumber(9);
        } else if (id == R.id.main_btn_back) {
            back();
        } else if (id == R.id.main_btn_clear) {
            clear();
        } else if (id == R.id.main_btn_send) {
            send();
        } else if (id == R.id.main_btn_replay) {
            initGame();
            clear();
            initListView();
        }
    }

    //產生謎底
    private List<Integer> createAnswer(){
        Set<Integer> nums = new HashSet<>();
        while (nums.size() < 4){
            nums.add((int)(Math.random()*10));
        }

        List<Integer> ret = new ArrayList<>(nums);
        Collections.shuffle(ret);
        Log.v("anser", ret.toString());
        return ret;
    }

    private int inputPoint = 0;
    private final List<Integer> inputValue = new ArrayList<>();
    private void inputNumber(int num){
        if (inputPoint == 4) return;

        inputValue.add(num);
        input[inputPoint].setText("" + num);
        inputPoint++;
        btnNumber[num].setEnabled(false);
    }

    private void clear(){
        if (inputPoint == 0) return;

        inputPoint = 0;

        for (Integer i : inputValue){
            btnNumber[i].setEnabled(true);
        }

        inputValue.clear();

        for (TextView textView : input) {
            textView.setText("");
        }
    }

    private void back(){
        if (inputPoint == 0) return;
        inputPoint--;
        input[inputPoint].setText("");
        btnNumber[inputValue.get(inputPoint)].setEnabled(true);
        inputValue.remove(inputPoint);
    }

    private void send(){
        if (inputValue.size() != 4) return;

        int A = 0;
        int B = 0;
        StringBuilder guess = new StringBuilder();
        for (int i = 0; i < inputValue.size(); i++){
            guess.append(inputValue.get(i));
            if (inputValue.get(i).equals(anser.get(i))){
                A++;
            } else if (anser.contains(inputValue.get(i))){
                B++;
            }
        }

        Map<String, String> maps = new HashMap<>();
        maps.put(from[0], "" + (data.size()+1));
        maps.put(from[1], guess.toString());
        maps.put(from[2], A + "A" + B + "B");
        data.add(maps);
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(data.size() - 1);
        clear();

        if (A == 4){
            //Winner
            displayResult(true, guess);
        } else if (data.size() == 10) {
            //Loser
            displayResult(false, guess);
        }
    }

    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, String>> data;
    private final String[] from = {"order", "guess", "result"};
    private final int[] to = {R.id.item_order, R.id.item_guess, R.id.item_result};
    private void initListView(){
        listView = findViewById(R.id.main_listview);
        data = new ArrayList<>();
        adapter = new SimpleAdapter(this, data, R.layout.item_round, from, to);
        listView.setAdapter(adapter);
    }

    private void displayResult(boolean isWinner, StringBuilder sb){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("遊戲結果");

        builder.setMessage(isWinner? "完全正確":"挑戰失敗\n" + "答案" + sb);
        builder.setPositiveButton("開新局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initGame();
                clear();
                initListView();
            }
        }).show();
    }
}