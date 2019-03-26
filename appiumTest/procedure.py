from time import sleep


class Procedure:
    def __init__(self,driver):
        self.driver = driver

    def __enter_tasks_list(self):
        sleep(2)
        self.driver.find_element_by_id('com.workmanagerjm:id/main_recycler_view').click()
        self.driver.find_element_by_name('')



    def test(self):
        try:
            self.__enter_tasks_list()
        except Exception,Argument:
            print Argument
