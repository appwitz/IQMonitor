package com.hack.iqmonitor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

	@SerializedName("question_hash")
	@Expose
	private String questionHash;
	@Expose
	private String text;
	@Expose
	private List<String> answers = new ArrayList<String>();
	@Expose
	private List<String> urls = new ArrayList<String>();
	@Expose
	private String background;

	/**
	 * 
	 * @return The questionHash
	 */
	public String getQuestionHash() {
		return questionHash;
	}

	/**
	 * 
	 * @param questionHash
	 *            The question_hash
	 */
	public void setQuestionHash(String questionHash) {
		this.questionHash = questionHash;
	}

	/**
	 * 
	 * @return The text
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 *            The text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @return The answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	/**
	 * 
	 * @param answers
	 *            The answers
	 */
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	/**
	 * 
	 * @return The urls
	 */
	public List<String> getUrls() {
		return urls;
	}

	/**
	 * 
	 * @param urls
	 *            The urls
	 */
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	/**
	 * 
	 * @return The background
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * 
	 * @param background
	 *            The background
	 */
	public void setBackground(String background) {
		this.background = background;
	}

}
