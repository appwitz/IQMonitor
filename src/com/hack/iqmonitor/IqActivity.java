package com.hack.iqmonitor;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class IqActivity extends ActionBarActivity implements android.view.View.OnClickListener {

	TextView question, op1, op2, op3, op4;
	ImageView im1, im2, im3, im4 , qim;
	Falcon api;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

		setContentView(R.layout.activity_iq);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.setCancelable(false);
		dialog.show();
		api = new Falcon();
		question = (TextView) findViewById(R.id.question);
		op1 = (TextView) findViewById(R.id.op1);
		op2 = (TextView) findViewById(R.id.op2);
		op3 = (TextView) findViewById(R.id.op3);
		op4 = (TextView) findViewById(R.id.op4);
		
		

		im1 = (ImageView) findViewById(R.id.im1);
		im2 = (ImageView) findViewById(R.id.im2);
		im3 = (ImageView) findViewById(R.id.im3);
		im4 = (ImageView) findViewById(R.id.im4);
		qim = (ImageView) findViewById(R.id.qim);
		
		im1.setOnClickListener(this) ;
		im2.setOnClickListener(this) ;
		im3.setOnClickListener(this) ;
		im4.setOnClickListener(this) ;
		
		
		op1.setOnClickListener(this) ;
		op2.setOnClickListener(this) ;
		op3.setOnClickListener(this) ;
		op4.setOnClickListener(this) ;

		
		JSONObject message = new JSONObject();
		try {
			message.put("email", "sidduu"+(int)Math.random()*500+"tkarsh@gmail.com");
			// hel11"+(int)(Math.random()*500)+"o@test.com
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String result = api.send("POST", "/api/v1/falcon/respondent", message); // make
		Log.e("sdfdfs", "came in questions") ;																	// the
																				// respondent
		try {
			Log.e("sdfdfs", "came in questions") ;
			JSONObject splitMe = new JSONObject(result);
			String userHash = splitMe.getString("hash"); // store the hash
			Log.d("hash", userHash);
			JSONObject message2 = new JSONObject();
			message2.put("user_id", userHash);
			String result2 = api.send("POST", "/api/v1/falcon/question/get",
					message2); // get a question
			Log.d("question", result2);
			try {
				Gson gson = new Gson();
				Hiq hiq = gson.fromJson(result2, Hiq.class);
				if(hiq.getQuestions().get(0).getText().startsWith("image:")){
					question.setVisibility(View.GONE);
					qim.setVisibility(View.VISIBLE);
					new ImageDownloader(im1).execute(hiq.getQuestions().get(0).getText().substring(8));
				}else
				{
					question.setVisibility(View.VISIBLE);
					qim.setVisibility(View.GONE);
					question.setText(hiq.getQuestions().get(0).getText());
				}
				
				String ans = hiq.getQuestions().get(0).getAnswers().get(0);
				if (ans.startsWith("image:")) {
					op1.setVisibility(View.GONE);
					op2.setVisibility(View.GONE);
					op3.setVisibility(View.GONE);
					op4.setVisibility(View.GONE);
					
					im1.setVisibility(View.VISIBLE);
					im2.setVisibility(View.VISIBLE);
					im3.setVisibility(View.VISIBLE);
					im4.setVisibility(View.VISIBLE);
					Log.v(null, hiq.getQuestions().get(0).getAnswers().get(0)
							.substring(8));
					new ImageDownloader(im1).execute(hiq.getQuestions().get(0)
							.getAnswers().get(0).substring(8));
					new ImageDownloader(im2).execute(hiq.getQuestions().get(0)
							.getAnswers().get(1).substring(8));
					new ImageDownloader(im3).execute(hiq.getQuestions().get(0)
							.getAnswers().get(2).substring(8));
					new ImageDownloader(im4).execute(hiq.getQuestions().get(0)
							.getAnswers().get(3).substring(8));
				} else {
					throw new Exception() ;
//					op1.setVisibility(View.VISIBLE);
//					op2.setVisibility(View.VISIBLE);
//					op3.setVisibility(View.VISIBLE);
//					op4.setVisibility(View.VISIBLE);
//					im1.setVisibility(View.GONE);
//					im2.setVisibility(View.GONE);
//					im3.setVisibility(View.GONE);
//					im4.setVisibility(View.GONE);
//					op1.setText(hiq.getQuestions().get(0)
//							.getAnswers().get(0));
//					op2.setText(hiq.getQuestions().get(0)
//							.getAnswers().get(1));
//					op3.setText(hiq.getQuestions().get(0)
//							.getAnswers().get(2));
//					op4.setText(hiq.getQuestions().get(0)
//							.getAnswers().get(3));
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				question.setText("Identify the Indian flag");
				op1.setVisibility(View.GONE);
				op2.setVisibility(View.GONE);
				op3.setVisibility(View.GONE);
				op4.setVisibility(View.GONE);
				im1.setVisibility(View.VISIBLE);
				im2.setVisibility(View.VISIBLE);
				im3.setVisibility(View.VISIBLE);
				im4.setVisibility(View.VISIBLE);
				new ImageDownloader(im1)
						.execute("https://lh3.googleusercontent.com/-Y1CECwu_Au8/UzJ3mc7BskI/AAAAAAAAAZA/z5nosRkawTY/s426/South-Africa-Flag-icon.png");
				new ImageDownloader(im2)
						.execute("http://www.wewillspeakout.org/wp-content/themes/wewillspeakout/flags/Rwanda-Flag-256.png");
				new ImageDownloader(im3)
						.execute("http://img.freeflagicons.com/thumb/glossy_square_icon/india/india_640.png");
				new ImageDownloader(im4)
						.execute("http://internationalstudyexperts.com/images/aust.png");
			}
			JSONObject splitMe2 = new JSONObject(result2);
			JSONArray questions = splitMe2.getJSONArray("questions");
			JSONObject question = questions.getJSONObject(0);
			String questionHash = question.getString("question_hash"); // store
																		// the
																		// hash
			Log.d("question_hash", questionHash);

			JSONObject message3 = new JSONObject(); // set up the answer
			JSONArray answers = new JSONArray();
			JSONObject answer = new JSONObject();
			answer.put("question_hash", questionHash);
			answer.put("answer", 0);
			answers.put(answer);
			message3.put("user_id", userHash);
			message3.put("answers", answers);
			Log.d("json", message3.toString());

			String result3 = api
					.send("POST", "/api/v1/falcon/answer", message3); // send it
																		// in!
			Log.d("final result", result3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iq, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		ProgressDialog dialog;

		public ImageDownloader(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				mIcon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (dialog == null) {
				dialog = new ProgressDialog(IqActivity.this);
				dialog.setMessage("Loading...");
				dialog.show();
			}

		}
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Your answer is correct");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               finish() ;
            }
        });
        

        AlertDialog alert11 = builder1.create();
        alert11.setCancelable(false) ;
        alert11.show();
		
		
	}
	
	
	
	

	

	
}
