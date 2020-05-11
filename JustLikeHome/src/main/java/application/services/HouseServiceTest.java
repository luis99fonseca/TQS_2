package application.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import application.repositories.HouseRepository;



@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock(lenient = true)
    private HouseRepository houseRepository;

    @InjectMocks
    private HouseService houseService;

}