package be.deschutter.scimitar.alliance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AllianceServiceImplTest {
    @InjectMocks
    private AllianceServiceImpl allianceService;
    @Mock
    private AllianceEao allianceEao;

    @Test
    public void findBy() throws Exception {
        final Alliance alliance = new Alliance("allianceName");
        when(allianceEao.findByAllianceNameIgnoreCaseContaining("allianceName"))
            .thenReturn(alliance);
        assertThat(allianceService.findBy("allianceName")).isSameAs(alliance);
    }

    @Test(expected = AllianceNotFoundException.class)
    public void findBy_notFound() throws Exception {
        try {
            allianceService.findBy("allianceName");
        } catch (AllianceNotFoundException e) {
            assertThat(e.getAllianceName()).isEqualTo("allianceName");
            throw e;
        }
        fail("should have throw an exception");
    }

}