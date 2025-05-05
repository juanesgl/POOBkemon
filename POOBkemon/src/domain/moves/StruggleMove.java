package domain.moves;

import domain.entities.Pokemon;
import domain.enums.MoveCategory;
import domain.enums.PokemonType;

public class StruggleMove extends Move {

    public StruggleMove() {
        super(
            "Struggle",               // name
            50,                       // power
            MoveCategory.PHYSICAL,    // category (Struggle se trata como físico)
            PokemonType.NORMAL,       // type (Struggle es de tipo normal)
            100,                      // accuracy (siempre acierta, puedes usar 100)
            Integer.MAX_VALUE,        // powerPoints (no tiene límite real de uso)
            0                         // priority (prioridad normal)
        );
    }

   /* @Override
    public void use(Pokemon attacker, Pokemon target) {
        int damage = attacker.calculateDamage(this, target);
        target.takeDamage(damage);

        int recoil = damage / 2;
        attacker.takeDamage(recoil);
    }*/
}
