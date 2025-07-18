package jayslabs.section8.entity;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.relational.core.mapping.Table;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @ToString
// @Table("customer")
// public class Customer {

//     @Id 
//     private Integer id;
//     private String name;
//     private String email;
// }


import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    private Integer id;
    private String name;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}