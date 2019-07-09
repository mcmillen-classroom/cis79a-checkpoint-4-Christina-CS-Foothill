package christinahunter.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mHintTextView;
    private TextView mQuestionTextView;
    private Question currQuestion;
    private TextView mScoreView;
    private LinearLayout mTrueFalseContainer;
    private LinearLayout mFillTheBlankContainer;
    private LinearLayout mMultipleChoiceContainer;
    private EditText mEditText;
    private Button mCheckButton;
    private int mScore = 0;
    private TextView mQuestionStatusView;
    private Question[] mQuestionBank = new Question[8];
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        mQuestionBank[0] = new TrueFalseQuestion(R.string.question_1,false);
        mQuestionBank[1] = new TrueFalseQuestion(R.string.question_2,true);
        mQuestionBank[2] =  new  TrueFalseQuestion(R.string.question_3, R.string.question_3_hint,false);
        mQuestionBank[3] = new  TrueFalseQuestion(R.string.question_4, false);
        mQuestionBank[4] = new  TrueFalseQuestion(R.string.question_5, R.string.question_5_hint, false);
        mQuestionBank[5] = new  TrueFalseQuestion(R.string.question_6, R.string.question_6_hint,true);
        mQuestionBank[6] = new  TrueFalseQuestion(R.string.question_7, true);
        //local variable to store answers for question 8
        //call the the resource type 'array' even though it's in the string folder
        //cannot call getResources() method until after Android constructors(not shown for Activities) are run
        //this is why the code was moved into the onCreate() method
        String[] q8Answers = getResources().getStringArray(R.array.question_8_answers);
        mQuestionBank[7] = new FillTheBlankQuestion(R.string.question_8,R.string.question_8_hint,q8Answers);

        //set up the buttons, textviews and layouts/containers
        mQuestionStatusView = (TextView) findViewById(R.id.question_status);
        //question and hint text views
        mQuestionTextView = (TextView) findViewById(R.id.text_view);
        mHintTextView = (TextView) findViewById(R.id.hint_view);
        //true false container
        mTrueFalseContainer = (LinearLayout) findViewById(R.id.true_false_container);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        //fill the blank container
        mFillTheBlankContainer = (LinearLayout) findViewById(R.id.fill_the_blank_container);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mCheckButton = (Button) findViewById(R.id.check_button);
        //multiple choice container
        mMultipleChoiceContainer = (LinearLayout) findViewById(R.id.multiple_choice_container);
        //previous and next button container
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mScoreView = (TextView) findViewById(R.id.score_view);


        //now display the first question
        setUpQuestion();

        //set the click listeners
        mTrueButton.setOnClickListener(this);
        mFalseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mHintTextView.setOnClickListener(this);
        mCheckButton.setOnClickListener(this);


    }

    public void setUpQuestion(){
        currQuestion = mQuestionBank[mCurrentIndex];
        mQuestionTextView.setText(currQuestion.getTextResId());
        mHintTextView.setText(R.string.hint_default_text);
        if(currQuestion.isHasBeenAnswered())
            mQuestionStatusView.setText(R.string.question_status_answered);
        else
            mQuestionStatusView.setText("");

        if(currQuestion.isTrueFalseQuestion()){
            mTrueFalseContainer.setVisibility(View.VISIBLE);
            mFillTheBlankContainer.setVisibility(View.GONE);
            mMultipleChoiceContainer.setVisibility(View.GONE);
        }
        else if(currQuestion.isFillTheBlankQuestion()){
            mTrueFalseContainer.setVisibility(View.GONE);
            mFillTheBlankContainer.setVisibility(View.VISIBLE);
            mMultipleChoiceContainer.setVisibility(View.GONE);
        }
        else if(currQuestion.isMultipleChoiceQuestion()){
            mTrueFalseContainer.setVisibility(View.GONE);
            mFillTheBlankContainer.setVisibility(View.GONE);
            mMultipleChoiceContainer.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.true_button && !currQuestion.isHasBeenAnswered()) {
            checkAnswer(true);
        }
        else if(v.getId() == R.id.false_button && !currQuestion.isHasBeenAnswered()){
            checkAnswer(false);
        }
        else if(v.getId() == R.id.check_button){

            if(!currQuestion.isHasBeenAnswered())
                checkAnswer(mEditText.getText().toString());

        }
        else if(v.getId() == R.id.next_button){
            //change to next question
            mCurrentIndex++;

            if(mCurrentIndex == mQuestionBank.length){
                mCurrentIndex = 0;
                mScore = 0;
                mScoreView.setText("Score: " + mScore);
                showToast("Quiz restarted!");
                resetQuestions();
            }
            setUpQuestion();
        }
        else if(v.getId() == R.id.previous_button){
            //change to previous question

            if(mCurrentIndex == 0){
                mCurrentIndex = (mQuestionBank.length);
            }

            mCurrentIndex--;
            setUpQuestion();
        }
        else if(v.getId() == R.id.hint_view){
            if(currQuestion.getmHintResId() == -1){
                mHintTextView.setText(R.string.hint_unavailable_text);
            }
            else{
                mHintTextView.setText(currQuestion.getmHintResId());
            }
        }
    }

    public boolean checkAnswer(boolean userInput){

        if(currQuestion.checkAnswer(userInput)){
            showToast("You are correct");
            mScore++;
            mScoreView.setText("Score: " + mScore);
            currQuestion.setHasBeenAnswered(true);
            return true;
        }
        else{
            showToast("You are incorrect");
            if(mScore > 0)
                mScore--;
            mScoreView.setText("Score: " + mScore);
            currQuestion.setHasBeenAnswered(true);
            return false;
        }

    }

    public boolean checkAnswer(String userInput){

        if(currQuestion.checkAnswer(userInput)){
            showToast("You are correct");
            mScore++;
            mScoreView.setText("Score: " + mScore);
            currQuestion.setHasBeenAnswered(true);
            return true;
        }
        else{
            showToast("You are incorrect");
            if(mScore > 0)
                mScore--;
            mScoreView.setText("Score: " + mScore);
            currQuestion.setHasBeenAnswered(true);
            return false;
        }

    }

    public void showToast(String s){
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP,0,0);
        myToast.show();
    }

    public void resetQuestions(){

        for(Question q: mQuestionBank)
            q.setHasBeenAnswered(false);


        mQuestionStatusView.setText("");
        mEditText.setText("");

    }
}
