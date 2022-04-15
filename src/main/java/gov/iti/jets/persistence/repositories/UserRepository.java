package gov.iti.jets.persistence.repositories;

import gov.iti.jets.persistence.entities.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends AbstractRepository<User>{
    public UserRepository( EntityManager entityManager ) {
        super( entityManager );
        this.setClazz( User.class );
    }
}
