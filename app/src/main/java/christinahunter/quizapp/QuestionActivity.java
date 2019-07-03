package christinahunter.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private int mScore = 0;
    private TextView mQuestionStatusView;

    private Question[] mQuestionBank = new Question[]{
            new TrueFalseQuestion(R.string.question_1,false),
            new  TrueFalseQuestion(R.string.question_2,true),
            new  TrueFalseQuestion(R.string.question_3, R.string.question_3_hint,false),
            new  TrueFalseQuestion(R.string.question_4, false),
            new  TrueFalseQuestion(R.string.question_5, R.string.question_5_hint, false),
            new  TrueFalseQuestion(R.string.question_6, R.string.question_6_hint,true),
            new  TrueFalseQuestion(R.string.question_7, true)
    };

    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mQuestionTextView = (TextView) findViewById(R.id.text_view);
        mHintTextView = (TextView) findViewById(R.id.hint_view);
        currQuestion = mQuestionBank[mCurrentIndex];
        int question = currQuestion.getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mScoreView = (TextView) findViewById(R.id.score_view);
        mQuestionStatusView = (TextView) findViewById(R.id.question_status);

        mTrueButton.setOnClickListener(this);
        mFalseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mHintTextView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.true_button && !currQuestion.isHasBeenAnswered()) {
            checkAnswer(true);
        }
        else if(v.getId() == R.id.false_button && !currQuestion.isHasBeenAnswered()){
            checkAnswer(false);
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
            currQuestion = mQuestionBank[mCurrentIndex];
            mQuestionTextView.setText(currQuestion.getTextResId());
            mHintTextView.setText(R.string.hint_default_text);

            if(currQuestion.isHasBeenAnswered())
                mQuestionStatusView.setText(R.string.question_status_answered);
            else
                mQuestionStatusView.setText("");
        }
        else if(v.getId() == R.id.previous_button){
            //change to previous question

            if(mCurrentIndex == 0){
                mCurrentIndex = (mQuestionBank.length);
            }

            mCurrentIndex--;
            currQuestion = mQuestionBank[mCurrentIndex];
            mQuestionTextView.setText(currQuestion.getTextResId());
            mHintTextView.setText(R.string.hint_default_text);
            if(currQuestion.isHasBeenAnswered())
                mQuestionStatusView.setText(R.string.question_status_answered);
            else
                mQuestionStatusView.setText("");
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

    public void showToast(String s){
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP,0,0);
        myToast.show();
    }

    public void resetQuestions(){

        for(Question q: mQuestionBank)
            q.setHasBeenAnswered(false);

        mQuestionStatusView.setText("");

    }
}
