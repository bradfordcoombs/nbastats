/**
 * 
 */
package com.bc.stats.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bc.stats.domain.Player;

/**
 * @author Bradford
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {

   List<Player> findByLastNameAndFirstNameAllIgnoreCase(String lastName, String firstName);
}
