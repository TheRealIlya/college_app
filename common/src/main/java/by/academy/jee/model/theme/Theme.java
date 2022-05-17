package by.academy.jee.model.theme;

import by.academy.jee.model.AbstractEntity;
import by.academy.jee.model.grade.Grade;
import by.academy.jee.model.group.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Theme extends AbstractEntity {

    private String title;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(mappedBy = "themes", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Group> groups;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "theme", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Grade> grades;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
