package netgloo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import netgloo.bean.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Integer>  {

}
