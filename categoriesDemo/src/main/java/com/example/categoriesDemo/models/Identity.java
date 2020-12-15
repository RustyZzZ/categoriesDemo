package com.example.categoriesDemo.models;

import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class Identity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true)
	private Category category;


	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		Identity identity = (Identity) o;
		return id.equals(identity.id) && Objects.equals(name, identity.name) && Objects.equals(description, identity.description) && Objects
				.equals(category, identity.category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}
}
