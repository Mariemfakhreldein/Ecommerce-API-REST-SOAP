package gov.iti.jets.persistence.repositories;

import gov.iti.jets.domain.models.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends AbstractRepository<User>{
    public UserRepository( EntityManager entityManager ) {
        super( entityManager );
        this.setClazz( User.class );
    }
}
