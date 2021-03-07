package io.neoOkpara.librado.ws.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users_tbl")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AuditModel {

	private static final long serialVersionUID = -4086767047093795760L;

	@Column(length = 25, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Gender gender;

	@Lob
	@Column(nullable = false, length = 80, columnDefinition = "TEXT", unique = true)
	private String email;

	@Column(nullable = false, length = 100)
	private String encryptedPassword;

	@Column(nullable = false, unique = true, length = 20)
	private String userId;

	@Column(nullable = false)
	@Builder.Default
	private boolean enabled = true;

	@Column(nullable = false)
	@Builder.Default
	private boolean accountNonExpired = true;

	@Column(nullable = false)
	@Builder.Default
	private boolean credentialsNonExpired = true;

	@Column(nullable = false)
	@Builder.Default
	private boolean accountNonLocked = true;

}
