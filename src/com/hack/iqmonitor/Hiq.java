package com.hack.iqmonitor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Hiq {

	@Expose
	private Object hash;
	@Expose
	private Object name;
	@Expose
	private Object sponsor;
	@Expose
	private Object type;
	@Expose
	private List<Question> questions = new ArrayList<Question>();

	/**
	 * 
	 * @return The hash
	 */
	public Object getHash() {
		return hash;
	}

	/**
	 * 
	 * @param hash
	 *            The hash
	 */
	public void setHash(Object hash) {
		this.hash = hash;
	}

	/**
	 * 
	 * @return The name
	 */
	public Object getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(Object name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The sponsor
	 */
	public Object getSponsor() {
		return sponsor;
	}

	/**
	 * 
	 * @param sponsor
	 *            The sponsor
	 */
	public void setSponsor(Object sponsor) {
		this.sponsor = sponsor;
	}

	/**
	 * 
	 * @return The type
	 */
	public Object getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	public void setType(Object type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * 
	 * @param questions
	 *            The questions
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

}