package com.kaizen.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaizen.project.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User , Long> {

	public User findByEmail(String email);
	public User findByImmatricule(int immatricule);
	
	@Query(value="SELECT u.* FROM users u WHERE u.email = ?1", nativeQuery = true)
	public User showConnectedUserData(String email);
	@Query(value="SELECT * FROM users u WHERE u.immatricule != 99999 ORDER BY u.immatricule ASC", nativeQuery = true)
	public Page<User> showAllUsersData(PageRequest pageRequest);
	@Query(value="SELECT u.* FROM users u WHERE u.department = ?1", nativeQuery = true)
	public List<User> TeamMember(String department);
	@Query(value="SELECT count(*) FROM users u WHERE u.department = ?1", nativeQuery = true)
	public int TeamMemberCount(String department);
	@Query(value="SELECT count(*) FROM users u", nativeQuery = true)
	public int TotalUserCount();
	@Query(value="SELECT count(*) FROM users u INNER JOIN users_roles ur ON ur.user_id = u.id INNER JOIN role r ON ur.role_id = r.id WHERE r.name = 'ROLE_ADMIN'", nativeQuery = true)
	public int TotalAdminCount();
	@Query(value="SELECT * FROM users u INNER JOIN users_roles ur ON ur.user_id = u.id INNER JOIN role r ON ur.role_id = r.id WHERE r.name = 'ROLE_ADMIN'", nativeQuery = true)
	public List<User> AllAdmin();
	@Query(value="SELECT u.*,email FROM users u INNER JOIN users_roles ur ON ur.user_id = u.id INNER JOIN role r ON ur.role_id = r.id WHERE r.name = 'ROLE_ADMIN' AND u.email LIKE %:email%", nativeQuery = true)
	public List<User> AdminsAutocomplete(String email);
	@Query(value="SELECT * FROM users u INNER JOIN users_roles ur ON ur.user_id = u.id INNER JOIN role r ON ur.role_id = r.id WHERE r.name = 'ROLE_USER' AND u.department = ?1 ", nativeQuery = true)
	public List<User> GetUsersByDepartment(String department);

	
	/*@Query(value="DELETE users_roles ur set ur.role_id = 12 WHERE ur.user_id = ?1")
	public int UpdateUserRole(int id_user);*/
}
