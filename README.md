import boto3

# Set up the AWS credentials and region
AWS_ACCESS_KEY = 'your_access_key'
AWS_SECRET_KEY = 'your_secret_key'
AWS_REGION = 'your_aws_region'

# Initialize the S3 client
s3_client = boto3.client(
    's3',
    aws_access_key_id=AWS_ACCESS_KEY,
    aws_secret_access_key=AWS_SECRET_KEY,
    region_name=AWS_REGION
)

# Define the bucket and file to upload
bucket_name = 'your_bucket_name'
file_path = 'path/to/your/file.txt'
file_key = 'destination/file.txt'  # The path inside the bucket

# Upload the file to S3
s3_client.upload_file(file_path, bucket_name, file_key)

print(f"File uploaded to {bucket_name}/{file_key}")
