package madbarsoft.com.myappproto;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    Button btnNext, btnBack;
    private NextAndBackDetailsListenerInteF nextAndBackDetailsListenerInteF;
    Person person;
    private Context context;
    private int currentDataPosition;
    QuestionAndAns questionAndAns;
    List<QuestionAndAns> questionAndAnsList = new ArrayList<>();

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questionAndAns = (QuestionAndAns) getArguments().getSerializable("questionAndAns");
        try {
            generateQuestionAndAnswerData(questionAndAns.getCategoryId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

     /*   if(questionAndAns==null){
            questionAndAns = questionAndAnsList.get(0);
        }*/
        this.currentDataPosition = getArguments().getInt("currentDataPosition");
        View vu = inflater.inflate(R.layout.fragment_details, container, false);
        TextView ansTextView = vu.findViewById(R.id.detailsShowTxVuAnsId);
        if(questionAndAns !=null){
         //   qstTextView.setText("Q ) "+questionAndAns.getQst().toString());
            ansTextView.setText("Q ) "+questionAndAns.getQst().toString()+"\nAns ) "+questionAndAns.getAns().toString());
           //textView.setText("Q) "+questionAndAns.getQst().toString()+"\n Ans: "+questionAndAns.getAns().toString());
        }

        btnNext = (Button)vu.findViewById(R.id.btnNextId);
        btnBack = (Button)vu.findViewById(R.id.btnBackId);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getContext(), "Msg: You Click Next Button, Pos:"+currentDataPosition, Toast.LENGTH_SHORT).show();
                if((questionAndAnsList.size()-1) != currentDataPosition) {
                    nextAndBackDetailsListenerInteF.nextData(questionAndAnsList.get((currentDataPosition + 1)), (currentDataPosition + 1));
                    return;
                }
                nextAndBackDetailsListenerInteF.nextData(questionAndAnsList.get((currentDataPosition)), (currentDataPosition));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentDataPosition!=0){
                    nextAndBackDetailsListenerInteF.backData(questionAndAnsList.get(currentDataPosition-1), currentDataPosition-1);
                    return;
                }
                nextAndBackDetailsListenerInteF.backData(questionAndAnsList.get(currentDataPosition), currentDataPosition);
            }
        });

        return vu;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        nextAndBackDetailsListenerInteF= (NextAndBackDetailsListenerInteF)context;
    }

//    private List<Person> generateList(){
//        List<Person> personList = new ArrayList<>();
//        personList.add(new Person("MD IMRAN HOSSAIN", 28, "Shariatpur"));
//        personList.add(new Person("MD MONIR HOSSAIN", 30, "Narayangong"));
//        personList.add(new Person("MD Biddyut HOSSAIN", 28, "Damudya"));
//        personList.add(new Person("MD Misty HOSSAIN", 28, "South Damudya"));
//        personList.add(new Person("MD Mithila Madbar", 28, "Dhaka"));
//        personList.add(new Person("MD Nabila Madbar", 28, "Dhaka"));
//        personList.add(new Person("MD Papeya Madbar", 28, "Dhaka"));
//        personList.add(new Person("MD Bristy Rashid", 28, "Dhaka"));
//        return personList;
//    }

    public void generateQuestionAndAnswerData(int categoryId) throws IOException, JSONException {
        String json;
        InputStream inputStream=null;
        if(categoryId==11){
            inputStream = getResources().openRawResource(R.raw.file_networking);
        }else if(categoryId==22){
            inputStream = getResources().openRawResource(R.raw.file_operatingsystem);
        }else if(categoryId==10){
            inputStream = getResources().openRawResource(R.raw.computer_bangladesh);
        }else{
            inputStream = getResources().openRawResource(R.raw.question_answer_empty_data);
        }
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        json = new String(buffer, "UTF-8");
        JSONArray jsonArray=new JSONArray(json);

        for(int i=0; i<jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            // personList.add(new Person(obj.getString("qst"),Integer.parseInt(obj.getString("sNo")), obj.getString("ans")));
            questionAndAnsList.add(new QuestionAndAns(Integer.parseInt(obj.getString("sNo")), Integer.parseInt(obj.getString("categoryId")), obj.getString("qst").toString(), obj.getString("ans").toString()));
        }

        //  Toast.makeText(this, "Data: "+dataList.toString(), Toast.LENGTH_SHORT).show();
    }

}
