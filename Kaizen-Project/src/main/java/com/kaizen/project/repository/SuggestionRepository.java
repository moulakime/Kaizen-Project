package com.kaizen.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaizen.project.model.Department;
import com.kaizen.project.model.Suggestion;
import com.kaizen.project.model.Type;

@Repository("suggestionRepository")
public interface SuggestionRepository extends JpaRepository<Suggestion , Long>{
	
	@Query(value="SELECT * FROM suggestions s inner join users u on s.user_id = u.id inner join types t on s.type_id = t.id ORDER BY s.created_at ASC", nativeQuery = true)
	public Page<Suggestion> showAllSuggestions(PageRequest pageRequest);
	@Query(value="SELECT count(*) FROM suggestions s inner join users u WHERE s.user_id = u.id AND s.receipent_user = ?1", nativeQuery = true)
	public int countSuggReceived(String email);
	@Query(value="SELECT count(*) FROM suggestions s inner join users u WHERE s.user_id = u.id AND s.receipent_user = ?1 AND s.choices = 'Pending'", nativeQuery = true)
	public int countSuggReceivedNotReplied(String email);
	@Query(value="SELECT count(*) FROM suggestions s inner join users u WHERE s.user_id = u.id AND u.id = ?1", nativeQuery = true)
	public int countSuggSent(Long id);
	@Query(value="SELECT count(*) FROM suggestions s inner join users u WHERE s.user_id = u.id AND s.user_id = ?1 AND s.choices = 'Pending'", nativeQuery = true)
	public int countSuggSentNoFeedBack(Long id);
	@Query(value="SELECT * FROM suggestions s inner join users u on s.user_id = u.id inner join types t on s.type_id = t.id AND u.id = ?1", nativeQuery = true)
	public Page<Suggestion> SuggestionSent(Long userid, PageRequest pageRequest);
	@Query(value="SELECT * FROM suggestions s inner join users u on s.user_id = u.id inner join types t on s.type_id = t.id AND s.receipent_user = ?1", nativeQuery = true)
	public Page<Suggestion> SuggestionReceived(String email, PageRequest pageRequest);
	@Query(value="SELECT count(*) FROM suggestions", nativeQuery = true)
	public int countAllSugg();
	@Query(value="SELECT count(*) FROM suggestions s WHERE s.choices = 'Approved' OR s.choices = 'Refused' ", nativeQuery = true)
	public int countTotalCompleted();
	@Query(value="SELECT count(*) FROM suggestions s WHERE s.choices = 'Pending'", nativeQuery = true)
	public int countTotalWaiting();
	//@Query(value="SELECT count(*) as COUNT,t.sugg_type FROM suggestions s INNER JOIN types t ON s.type_id = t.id GROUP BY t.sugg_type", nativeQuery = true)
	//public List<Suggestion> CountSuggestionByType();
	@Query(value="SELECT DISTINCT s.*,u.department FROM suggestions s INNER JOIN users u ON s.user_id = u.id AND u.department = ?1", nativeQuery = true)
	public List<Suggestion> ReportingBySuggestion(String department);
	@Query(value="SELECT DISTINCT s.*,u.department FROM suggestions s INNER JOIN users u ON s.user_id = u.id AND u.department = ?1 AND s.choices = ?2 AND s.type_id = ?3 AND s.created_at BETWEEN ?4 AND ?5", nativeQuery = true)
	public List<Suggestion> ReportingBySuggestionDepartmentStatus(String department, String status,Long type,String date_begin,String date_end);
	@Query(value="SELECT DISTINCT s.* FROM suggestions s WHERE s.choices = ?1", nativeQuery = true)
	public List<Suggestion> ReportingBySuggestionStatus(String status);
	
	/* ----- Just a Comment ------- */
	
	@Query(value="SELECT IFNULL(s.TOTAL,0) TOTAL FROM (SELECT departments.id,departments.department_name FROM departments ) d LEFT JOIN (SELECT u.department,COUNT(s.user_id) AS TOTAL FROM suggestions s INNER JOIN users u ON s.user_id = u.id GROUP BY u.department ORDER BY s.user_id) s ON d.department_name = s.department", nativeQuery = true)
	public List<String> ChartPerDepartment();
	/* ---- Just a Comment ----- */
	@Query(value="SELECT IFNULL(s.TOTAL,0) TOTAL FROM (SELECT types.id,types.sugg_type FROM types ) t LEFT JOIN (SELECT type_id,COUNT(s.type_id) AS TOTAL FROM suggestions s  GROUP BY s.type_id ORDER BY type_id) s ON t.id = s.type_id", nativeQuery = true)
	public List<String> ChartPerType();
	@Query(value="SELECT IFNULL(n.TOTAL,0) TOTAL FROM (SELECT 'January' AS MONTH UNION SELECT 'February' AS MONTH UNION SELECT 'March' AS MONTH UNION SELECT 'April' AS MONTH UNION SELECT 'May' AS MONTH UNION SELECT 'June' AS MONTH UNION SELECT 'July' AS MONTH UNION SELECT 'August' AS MONTH UNION SELECT 'September' AS MONTH UNION SELECT 'October' AS MONTH UNION SELECT 'November' AS MONTH UNION SELECT 'December' AS MONTH ) m LEFT JOIN (SELECT MONTHNAME(s.created_at) AS MONTH, COUNT(MONTHNAME(s.created_at)) AS TOTAL FROM suggestions s WHERE s.created_at BETWEEN '2019-01-01' AND '2019-12-31' GROUP BY MONTHNAME(s.created_at),MONTH(s.created_at) ORDER BY MONTH(s.created_at)) n ON m.MONTH=n.MONTH", nativeQuery = true)
	public List<String> ChartPerMonth();
	

}
