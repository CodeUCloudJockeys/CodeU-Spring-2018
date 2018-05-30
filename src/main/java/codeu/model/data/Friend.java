package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

public class Friend{
	public final UUID id;
	public final String name;

	public Friend(UUID id, String name){
		this.id = id;
		this.name = name;
	}

	//returns id of the friend user
	public UUID getId(){
		return id;
	}

	//returns friends name
	public String getName(){
		return name;
	}
}