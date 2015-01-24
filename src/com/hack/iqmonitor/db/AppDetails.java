package com.hack.iqmonitor.db;

import com.j256.ormlite.field.DatabaseField;

public class AppDetails {

	@DatabaseField(generatedId = true, canBeNull = false)
	private int id;
	@DatabaseField(canBeNull = true)
	private String appName;
	@DatabaseField(canBeNull = true)
	private String packageName;
	@DatabaseField(canBeNull = true)
	private long timeStamp;
	@DatabaseField(canBeNull = true)
	private long usageTime;
	@DatabaseField(canBeNull = true)
	private String category;
	@DatabaseField(canBeNull = true)
	private long sessionId;
	@DatabaseField(canBeNull = true)
	private long cancellationId;
	@DatabaseField(canBeNull = true)
	private long limitUsageTime;

	public AppDetails() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getUsageTime() {
		return usageTime;
	}

	public void setUsageTime(long usageTime) {
		this.usageTime = usageTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public long getCancellationId() {
		return cancellationId;
	}

	public void setCancellationId(long cancellationId) {
		this.cancellationId = cancellationId;
	}

	public long getLimitUsageTime() {
		return limitUsageTime;
	}

	public void setLimitUsageTime(long limitUsageTime) {
		this.limitUsageTime = limitUsageTime;
	}

}
