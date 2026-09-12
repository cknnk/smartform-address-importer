package cz.smartform.addressimporter.repository;

import cz.smartform.addressimporter.entity.Obec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObecRepository extends JpaRepository<Obec, Integer> {
}