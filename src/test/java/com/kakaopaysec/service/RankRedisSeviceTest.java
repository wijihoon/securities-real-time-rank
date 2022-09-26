/**
 * 
 */
package com.kakaopaysec.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author wijihoon
 *
 */
@ExtendWith(SpringExtension.class)
class RankRedisSeviceTest {

	private static final int AMOUNT = 3000;

//    @InjectMocks
//    private FundService fundService;
//
//    @Mock
//    private FundRepository fundRepository;
//
//    @Mock
//    private Fund fund;
//
//    @Mock
//    private HousingFinance housingFinance;
//
//    @Mock
//    private Institution institution;
//
//    @Test
//    @DisplayName("Fund 엔티티를 정상적으로 저장한다.")
//    void save_institution() {
//        given(fundRepository.save(any(Fund.class))).willReturn(fund);
//
//        fundService.save(housingFinance, institution, AMOUNT);
//
//        verify(fundRepository).save(any(Fund.class));
//    }
//
//    @Test
//    @DisplayName("년도별 지원 금액 통계를 정상적으로 조회한다.")
//    void find_annual_fund_statistics() {
//        fundService.findAllAnnualFundStatistics();
//
//        verify(fundRepository).findAllAnnualTotalFund();
//        verify(fundRepository).findAllAnnualInstitutionFund();
//    }
//    @Test
//    @DisplayName("Institution 엔티티를 정상적으로 저장한다.")
//    void save_institution() {
//        given(institutionRepository.save(any(Institution.class))).willReturn(institution);
//
//        institutionService.save(INSTITUTION_NAME, INSTITUTION_CODE);
//
//        verify(institutionRepository).save(any(Institution.class));
//    }
//
//    @Test
//    @DisplayName("Institution 객체 하나를 정상적으로 조회한다.")
//    void find_institution() {
//        given(institutionRepository.findById(any(Long.class))).willReturn(Optional.of(institution));
//
//        institutionService.findById(INSTITUTION_ID);
//
//        verify(institutionRepository).findById(INSTITUTION_ID);
//    }
//
//    @Test
//    @DisplayName("찾을 수 없는 Institution 객체를 조회하는 경우 예외를 발생한다.")
//    void find_institution_error() {
//        given(institutionRepository.findById(any(Long.class))).willReturn(Optional.empty());
//
//        assertThrows(NotFoundInstitutionException.class,
//                () -> institutionService.findById(INSTITUTION_ID));
//    }
//
//    @Test
//    @DisplayName("저장되어 있는 전체 Institution 객체를 정상적으로 조회한다.")
//    void find_institutions() {
//        given(institutionRepository.findAll()).willReturn(institutions);
//
//        institutionService.findAll();
//
//        verify(institutionRepository).findAll();
//    }
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.kakaopaysec.service.RankRedisSevice#init()}.
	 */
	@Test
	final void testInit() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.kakaopaysec.service.RankRedisSevice#getRankingByCondition(java.lang.String, int, int)}.
	 */
	@Test
	final void testGetRankingByCondition() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.kakaopaysec.service.RankRedisSevice#updateRandomRank()}.
	 */
	@Test
	final void testUpdateRandomRank() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait()}.
	 */
	@Test
	final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	@Test
	final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}

}
