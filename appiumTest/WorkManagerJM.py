
import os
import unittest
from appium import webdriver
from procedure import Procedure

Path = lambda p: os.path.abspath(
    os.path.join(os.path.dirname(__file__),p)
)

class WorkManagerJMTest(unittest.TestCase):
    def setUp(self):
        desired_cap = {}
        desired_cap['platformName'] = 'Android'
        desired_cap['platformVersion'] = '6.0.1'
        desired_cap['deviceName'] = 'aa'
        desired_cap['app'] = Path(
            'C:\\Users\\LiMeng\\Desktop\\GasTaskManager.apk'
        )

        self.driver = webdriver.Remote('http://localhost:4723/wd/hub',desired_cap)

    def tearDown(self):
        self.driver.quit()

    def test(self):
        Procedure(self.driver).test()


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(WorkManagerJMTest)
    unittest.TextTestRunner(verbosity=2).run(suite)

