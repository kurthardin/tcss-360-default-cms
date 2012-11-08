/**
 * 
 */
package edu.uwt.tcss360.Default.model;

import java.io.File;
import java.util.Date;
import java.util.List;

import edu.uwt.tcss360.Default.model.Paper;
import edu.uwt.tcss360.Default.model.User.Role;

/**
 * @author churlbong
 *
 */
public final class Conference {
	
	private final String my_name;
	private final String my_pc;
	private final Date my_start_date;
	private final Date my_end_date;
	private Date my_submission_deadline;		// TODO: Add get/set
	private Date my_review_deadline;			// TODO: Add get/set
	private Date my_recommendation_deadline;	// TODO: Add get/set
	private Date my_final_revision_deadline;	// TODO: Add get/set
	
	public Conference(final File a_data_file) {
		// TODO: Implement data file constructor
	}
	
	public Conference(final String the_pc_ser_id, final String the_name, final Date the_date) {
		this(the_pc_ser_id, the_name, the_date, the_date);
	}
	
	public Conference(String the_pc_ser_id, String the_name, Date the_start, Date the_end) {
		my_pc = the_pc_ser_id;
		my_name = the_name;
		my_start_date = the_start;
		my_end_date = the_end;
	}
	
	public String getname() {
		return my_name;
	}
	
	public Date getStartDate() {
		return (Date) my_start_date.clone();
	}
	
	public Date getEndDate() {
		return (Date) my_end_date.clone();
	}
	
	public Date getSubmissionDeadline() {
		return (Date) my_submission_deadline.clone();
	}
	
	public void setSubmissionDeadline(final Date the_deadline) {
		my_submission_deadline = (Date) the_deadline.clone();
	}
	
	public Date getReviewDeadline() {
		return (Date) my_review_deadline.clone();
	}
	
	public void setReviewDeadline(final Date the_deadline) {
		my_review_deadline = (Date) the_deadline.clone();
	}
	
	public Date getRecommendationDeadline() {
		return (Date) my_recommendation_deadline.clone();
	}
	
	public void setRecommendationDeadline(final Date the_deadline) {
		my_recommendation_deadline = (Date) the_deadline.clone();
	}
	
	public Date getFinalRevisionDeadline() {
		return (Date) my_final_revision_deadline.clone();
	}
	
	public void setFinalRevisionDeadline(final Date the_deadline) {
		my_final_revision_deadline = (Date) the_deadline.clone();
	}
	
	public boolean authorizeUser(String the_user_id, Role the_role) {
		// TODO: Implement authorizeUser()
	}
	
	public boolean deauthorizeUser(String the_user_id, Role the_role) {
		// TODO: Implement deauthorizeUser()
	}
	
	public List<String> getUserIds(Role a_role) {
		// TODO: Implement getUserIds()
	}
	
	public List<Paper> getPapers(String the_user_id, Role a_role) {
		// TODO: Implement getPapers()
	}
	
	public void addPaper(String the_author_id, Paper a_paper) {
		// TODO: Implement addPaper()
	}
	
	public void removePaper(Paper a_paper) {
		
	}
	
}
