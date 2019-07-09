package christinahunter.quizapp;

public class MultipleChoiceQuestion extends Question {

    int mOptionsResId;
    int mAnswer; //index into the array of correct answers

    public MultipleChoiceQuestion(int mTextResId, int hintResId,int optionsResId, int ans) {
        super(mTextResId,hintResId);
        mOptionsResId = optionsResId;
        mAnswer = ans;
    }

    @Override
    public boolean isMultipleChoiceQuestion(){
        return true;
    }
}
