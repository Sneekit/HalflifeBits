import psycopg2
import json

CONFIG_FILE = "Config/db_config.json"

class PostgresConnector:
	def __init__(self):
		self.connection = None
		self.load_config()

	def load_config(self):
		try:
			with open(CONFIG_FILE, 'r') as file:
				config = json.load(file)
				self.dbname = config['dbname']
				self.user = config['user']
				self.password = config['password']
				self.host = config['host']
				self.port = config['port']
		except Exception as e:
			print(f"Error loading config file: {e}")
			raise e

	def connect(self):
		try:
			self.connection = psycopg2.connect(
				dbname=self.dbname,
				user=self.user,
				password=self.password,
				host=self.host,
				port=self.port
			)
			result = self.execute_query("SELECT * FROM neurons LIMIT 1")
			print("Connected to database")
		except Exception as e:
			print(f"Error: Unable to connect to the database: {e}")

	def ensure_connection(self):
		if self.connection.closed == 1:
			print("Connection failed, restarting...")
			self.connect()
			print("Connected")

	def execute_query(self, query, values=None):
		try:
			self.ensure_connection()
			cursor = self.connection.cursor()
			if values is not None:
				if values is list:
					values = tuple(values)
				cursor.execute(query, values)
			else:
				cursor.execute(query)
			result = cursor.fetchall()
			cursor.close()
			return result
		except Exception as e:
			print(f"Error executing query: {e}")

	def execute_write(self, query, values=None):
		try:
			self.ensure_connection()
			cursor = self.connection.cursor()
			if values is not None:
				if type(values) is list:
					values = tuple(values)
				cursor.execute(query, values)
			else:
				cursor.execute(query)
			self.connection.commit()
			cursor.close()
		except Exception as e:
			print(f"Error executing write query: {e}")

	def close_connection(self):
		if self.connection is not None:
			self.connection.close()
			print("Connection closed")
