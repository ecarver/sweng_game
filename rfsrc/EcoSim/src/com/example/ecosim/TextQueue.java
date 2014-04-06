package com.example.ecosim;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TextQueue {
	View _view;
	private ArrayList<String> _textQueue;
	public TextQueue() {
		_textQueue = new ArrayList<String>();
	}
	
	@SuppressLint("InlinedApi") public void addText(View view, String text, int positionX, int positionY, int textSize, 
			int paddingOne, int paddingTwo, int paddingThree, int paddingFour) {
		
		_view = view;
		
		_textQueue.add(text);
		
		final RelativeLayout rl=(RelativeLayout) _view;

		
		TextView temp = new TextView(_view.getContext());
		
	    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
	            ((int)LayoutParams.WRAP_CONTENT,(int)LayoutParams.WRAP_CONTENT);
	    params.leftMargin=positionX;
	        params.topMargin=positionY;	
	        
	    temp.setText(text);
	    temp.setTextSize(textSize);
	    temp.setPadding(paddingOne, paddingTwo, paddingThree, paddingFour);
		temp.setLayoutParams(params);		
        //rl.removeViewInLayout(rl.getChildAt(2));
		rl.addView(temp);
	}
	
	public void removeText(String text) {
		
		int index = -1;
		for(int x = 0; x < _textQueue.size(); x++) {
			if(_textQueue.get(x).equals(text)) {
				index = x;
				break;
			}
		}
		
		if(index != -1) {
			ViewGroup temp = (ViewGroup) _view;
			_textQueue.remove(index);
			temp.removeViewAt(index+1);		
		}

	}
}


/*
final String[] str={"one","two","three","asdfgf"};
final RelativeLayout rl=(RelativeLayout) findViewById(R.id.gamelayout);
final TextView[] tv=new TextView[10];

for(int i=0;i<str.length;i++)
{
    tv[i]=new TextView(this);   
    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
            ((int)LayoutParams.WRAP_CONTENT,(int)LayoutParams.WRAP_CONTENT);
    params.leftMargin=50;
        params.topMargin=i*50;
    tv[i].setText(str[i]);
    tv[i].setTextSize((float) 20);
    tv[i].setPadding(20, 50, 20, 50);
    tv[i].setLayoutParams(params);
    rl.addView(tv[i]);

}
*/