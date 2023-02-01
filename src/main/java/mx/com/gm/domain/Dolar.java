package mx.com.gm.domain;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "dolar")
public class Dolar{

	
	@Id
	private int precio;
   
}
