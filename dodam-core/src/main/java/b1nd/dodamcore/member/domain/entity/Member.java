package b1nd.dodamcore.member.domain.entity;

import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private String id;

    @NotNull
    @JsonIgnore
    private String pw;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthStatus status;

    @NotNull
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    private String profileImage;

    @OneToOne(mappedBy = "member")
    private Student student;

    public void updateStatus(AuthStatus status) {
        this.status = status;
    }

    public void updateInfo(String imageUrl, String email) {
        this.profileImage = imageUrl;
        this.email = email;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    @Builder
    public Member(String id, String pw, String name, String email, MemberRole role, AuthStatus status,String profileImage) {
        Assert.hasText(id, "id must not be empty");
        Assert.hasText(pw, "pw must not be empty");
        Assert.hasText(name, "name must not be empty");
        Assert.hasText(email, "email must not be empty");
        Assert.notNull(role, "accessLevel must not be null");
        Assert.notNull(status, "allowed must not be empty");
        Assert.hasText(email, "email must have a text");

        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
    }
}
