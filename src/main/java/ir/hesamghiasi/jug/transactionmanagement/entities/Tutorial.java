package ir.hesamghiasi.jug.transactionmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tutorials")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tutorial {

	@Version
	private long version;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "description")
	private String description;
	@Column(name = "published")
	private boolean published;
}
