package com.whl.client.gateway.model.wallet;

public class WalletFaq {
	
	private String faqCategoryId	;
	private String faqCategoryName	;
	private int faqNum	;
	private String question	;
	private String answer	;
	private String updDate	;
	public String getFaqCategoryId() {
		return faqCategoryId;
	}
	public void setFaqCategoryId(String faqCategoryId) {
		this.faqCategoryId = faqCategoryId;
	}
	public String getFaqCategoryName() {
		return faqCategoryName;
	}
	public void setFaqCategoryName(String faqCategoryName) {
		this.faqCategoryName = faqCategoryName;
	}
	public int getFaqNum() {
		return faqNum;
	}
	public void setFaqNum(int faqNum) {
		this.faqNum = faqNum;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getUpdDate() {
		return updDate;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	@Override
	public String toString() {
		return "WalletFaq [faqCategoryId=" + faqCategoryId
				+ ", faqCategoryName=" + faqCategoryName + ", faqNum=" + faqNum
				+ ", question=" + question + ", answer=" + answer
				+ ", updDate=" + updDate + "]";
	}
	
	

}
